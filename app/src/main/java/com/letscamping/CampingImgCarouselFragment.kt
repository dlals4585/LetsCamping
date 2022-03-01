package com.letscamping

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.CampImgItemBinding
import com.letscamping.databinding.FragmentCampimgCarouselBinding
import com.letscamping.model.CampImg
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class CampingImgCarouselFragment : Fragment() {
    private lateinit var binding:FragmentCampimgCarouselBinding
    private var currentPosition = 0//Int.MAX_VALUE
    private var contentId:String = "2729"   //default data (Test)
    //private lateinit var campImg: ArrayList<CampImg>
    private var campImg: ArrayList<CampImg> = ArrayList<CampImg>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCampimgCarouselBinding.inflate(layoutInflater)
        //val view = inflater.inflate(R.layout.fragment_carousel,container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if(bundle != null){
            contentId = bundle.getString("contentId").toString()
        }
        if ((!contentId.equals(null)) && (contentId != "")){
            getCampImg(contentId)
        }

        binding.carouselLayout.setOnClickListener {
            Toast.makeText(requireContext(), "모두 보기 클릭했음", Toast.LENGTH_LONG).show()
        }
    }
    fun getCampImg(contentId:String) {
        println("contentId = $contentId")
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder().setLenient().create()  //MalformedJsonException 방지
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService().DEFAULT_URL)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        val server: retrofitAPI = retrofit.create(retrofitAPI::class.java)

        val params:HashMap<String,Any> = HashMap<String,Any>()
        params["contentId"] = contentId
        server.getSearchCamp("getimageCamplist",params).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){
                    try {
                        val res = response.body()
                        val items = res?.getAsJsonObject("items")?.asJsonObject
                        val bodyArray = items?.get("item")?.asJsonArray ?: JsonArray()
                        val parser = JsonParser()
                        val jsonObj = parser.parse(items.toString()) as JsonObject
                        val memberArray = jsonObj["item"] as JsonArray

                        for (i in 0 until bodyArray.size()){
                            val data1 = memberArray[i] as JsonObject
                            val campimg1: CampImg = CampImg(data1)
                            campImg.add(campimg1)
                        }
                        bindingdata(campImg)
                    } catch (e: JSONException) {
                        println("getCampImg JSONException : $e")
                    } catch (e1: java.lang.Exception) {
                        println("getCampImg Exception : $e1")
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //TODO("Not yet implemented")
                println("error : $t")
                println("error : ${call.request()}")
            }
        })
    }

    fun bindingdata(campImg: ArrayList<CampImg>){
        for (a in campImg){
            println("url_data == > ${a.jsonObject.get("imageUrl").asString.toString()}")
        }
        try{
            binding.textViewTotalBanner1.text = campImg.size.toString()
            binding.viewPagerBanner1.adapter = ViewPagerAdapter1(requireContext(),campImg)
            binding.viewPagerBanner1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.viewPagerBanner1.setCurrentItem(currentPosition, false) // 현재 위치를 지정
            // 현재 몇번째 배너인지 보여주는 숫자를 변경함
            binding.viewPagerBanner1.apply {
                registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                    @SuppressLint("SetTextI18n")
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        binding.textViewCurrentBanner1.text = "${(position%campImg.size)+1}"
                    }
                })
            }
        }catch (e:Exception){
            println("error:$e")
        }
    }

    class ViewPagerAdapter1(
        var context: Context,
        private val bannerList: ArrayList<CampImg>
        ) : RecyclerView.Adapter<ViewPagerAdapter1.PagerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            val itemBinding = CampImgItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PagerViewHolder(itemBinding)
        }

        override fun getItemCount(): Int = bannerList.size//Int.MAX_VALUE	// 아이템의 갯수를 임의로 확 늘린다.

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            //println("imageUrl==${item[position].jsonObject.get("imageUrl").asString.toString()}")
            if(bannerList.size!=0){
                val uri = Uri.parse(bannerList[position].jsonObject.get("imageUrl").asString.toString())
                Picasso.with(this.context).load(uri).into(holder.banner)
            }
            //holder.banner.setImageResource(item[position%item.size].jsonObject[""].asString.)	// position에 3을 나눈 나머지 값을 사용한다.
        }

        class PagerViewHolder(private val itemBinding: CampImgItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
            val banner = itemBinding.imageViewCamp
            init {
                banner.scaleType = ImageView.ScaleType.FIT_XY
            }
        }
    }
}
