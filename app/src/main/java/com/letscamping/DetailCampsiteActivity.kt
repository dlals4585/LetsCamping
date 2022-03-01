package com.letscamping

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.ActivityDetailCampsiteBinding
import com.letscamping.model.CampImg
import com.letscamping.model.CampingList
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DetailCampsiteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCampsiteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyword = intent.getStringExtra("facltNm")
        val contentId = intent.getStringExtra("contentId")
        println("content = $contentId")
        println("name_data=$keyword")

        binding = ActivityDetailCampsiteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_campsite)

        getCampDetail(keyword)
        setDataAtFragment(CampingImgCarouselFragment(),contentId)
        //supportFragmentManager.findFragmentById(R.id.camping_carousel_fragment) as CampingImgCarouselFragment
        binding.payment.setOnClickListener {
            Toast.makeText(applicationContext, "예약하기 클릭", Toast.LENGTH_LONG).show()
        }
    }

    fun setDataAtFragment(fragment: Fragment, data: String){
        val bundle = Bundle()
        //bundle.putStringArrayList("imageUrl", data)
        bundle.putString("contentId", data)
        fragment.arguments = bundle
        setFragment(fragment)
    }
    fun setFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null).detach(fragment).attach(fragment)
        transaction.setReorderingAllowed(true)//.replace(R.id.layout,fragment,fragment.javaClass.getSimpleName())
        transaction.commit()
        fragment.onStart()
    }
    @SuppressLint("SetTextI18n")
    fun dataset(campdetail: ArrayList<CampingList>) {
        val data = campdetail[0].jsonObject
        try {
            if(!data["facltNm"].asString.equals(null)){     //야영장 명
                binding.textview1.text = data["facltNm"].asString
                binding.facltNm.text = data["facltNm"].asString
            }
            if(!data["addr1"].asString.equals(null)) binding.addr.text = data["addr1"].asString //주소

            val mapy = data["mapY"].asDouble
            val mapx = data["mapX"].asDouble
            binding.gomap.setOnClickListener {
                startActivity(Intent(applicationContext, OpenKakaoMap::class.java).putExtra("GPSlat",mapy).putExtra("GPSlng",mapx))
            }
        }catch(e:Exception){
            println("error:기본정보 값 없음")
        }
        try{
            if(!data["lineIntro"].asString.equals(null)) binding.lineIntro.text = data["lineIntro"].asString    //설명
        }catch(e:Exception){
            println("error:설명 값없음")
        }
        try{
            if(!data["sbrsCl"].asString.equals(null)) { //편의시설 및 서비스
                if(!data["sbrsEtc"].asString.equals(null)) {
                    binding.sbrsCl.text = data["sbrsCl"].asString + " " + data["sbrsEtc"].asString
                }else{
                    binding.sbrsCl.text = data["sbrsCl"].asString
                }
            }
        }catch(e:Exception){
            println("error:편의시설 및 서비스 값없음")
        }
        try{
            if(!data["eqpmnLendCl"].asString.equals(null)) binding.eqpmnLendCl.text = data["eqpmnLendCl"].asString  //캠핑서비스
        }catch(e:Exception){
            println("error:캠핑서비스 값없음")
        }
        try{
            if(!data["posblFcltyCl"].asString.equals(null)) binding.posblFcltyCl.text = data["posblFcltyCl"].asString   //주변이용가능시설
        }catch(e:Exception){
            println("error:주변이용가능시설 값없음")
        }
    }

    fun getCampDetail(keyword:String) {
        val campdetail : ArrayList<CampingList> = ArrayList<CampingList>()
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
        params["pageNo"] = "1"
        params["keyword"] = keyword
        server.getSearchCamp("getSearchCamp",params).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){
                    val res = response.body()
                    //println("res:${res.toString()}")
                    val totalcount = res?.get("totalCount")?.asInt!!.toInt()
                    println("totalcount = $totalcount")
                    val items = res.getAsJsonObject("items").asJsonObject
                    if(totalcount<=1){
                        val item = items.get("item") as JsonObject
                        //println("items===  ${item.toString()}")
                        val camplist1: CampingList = CampingList(item)
                        campdetail.add(camplist1)
                    }else{
                        val bodyArray = items?.get("item")?.asJsonArray ?: JsonArray()
                        val parser = JsonParser()
                        val jsonObj = parser.parse(items.toString()) as JsonObject
                        val memberArray = jsonObj["item"] as JsonArray

                        try {
                            for (i in 0 until bodyArray.size()){
                                val data1 = memberArray[i] as JsonObject
                                val camplist1: CampingList = CampingList(data1)
                                campdetail.add(camplist1)
                            }
                        } catch (e: JSONException) {
                            println("getCampDetail JSONException : $e")
                        } catch (e1:Exception){
                            println("getCampDetail Exception : $e1")
                        }
                    }
                }
                dataset(campdetail)
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //TODO("Not yet implemented")
                println("error : $t")
                println("error : ${call.request()}")
            }
        })
    }
}