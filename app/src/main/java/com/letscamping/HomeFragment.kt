package com.letscamping

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.ActivityCampinglistBinding
import com.letscamping.databinding.FragmentHomeBinding
import com.letscamping.model.AreaInfo
import com.letscamping.model.CampingList
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var mContext : Context
    private lateinit var binding: FragmentHomeBinding
    /*override fun onAttach(context: Context?) {
        Log.d("Life_cycle", "F onAttach")
        super.onAttach(context)
    }*/

    override fun onCreate(savedInstanceState: Bundle?){
        Log.d("Life_cycle", "F onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Life_cycle", "F onCreateView")
        //>>>>> fragment가 인터페이스를 처음으로 그릴때 호출된다.  ****onCreateView
        //inflater = view를 그려주는것
        //container =  부모뷰(자식뷰에 붙여주는것)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        //val view = inflater.inflate(R.layout.fragment_home,container , false)
        val view = binding.root
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //onCreateView가 실행 된 다음 수행되는 주기(Fragment에서 액티비티에 대한 이벤트나 세팅은 여기에서 한다.)
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        getCamplist()

        binding.getHomeActivity.setOnClickListener {
            startActivity(Intent(it.context, MainTapActivity::class.java))
        }
        binding.mainSearch.setOnClickListener {
            startActivity(Intent(it.context, SearchTapActivity::class.java))//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        binding.mainNotice.setOnClickListener {
            startActivity(Intent(it.context, NoticeActivity::class.java))//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun getCamplist(){
        val camplist : ArrayList<CampingList> = ArrayList<CampingList>()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        var gson = GsonBuilder().setLenient().create()  //MalformedJsonException 방지
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitService().DEFAULT_URL)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        val server: retrofitAPI = retrofit.create(retrofitAPI::class.java)

        val params:HashMap<String,Any> = HashMap<String,Any>()
        //params.put()
        //server.addParamsToURL("home",params).enqueue(object : Callback<AreaInfo>{
        server.getArea("camplist").enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){
                    val res = response.body()
                    val bodyArray = res?.get("data")?.asJsonArray ?: JsonArray()

                    val parser = JsonParser()
                    val jsonObj = parser.parse(res.toString()) as JsonObject
                    val memberArray = jsonObj["data"] as JsonArray
                    //val areainfo = JSONObject(res.toString()).getString("data")

                    try {
                        for (i in 0 until bodyArray.size()){
                            val data1 = memberArray[i] as JsonObject
                            val camplist1: CampingList = CampingList(data1)
                            camplist.add(camplist1)
                        }
                    } catch (e: JSONException) {
                        println("JSONException : $e")
                    }
                }

                for (a in camplist){
                    println("name_data == > ${a.jsonObject.get("camp_defaddr").asString}")
                }

                val mainListadapter = HomeFragment.MainListView1Adapter(requireContext(), LayoutInflater.from(requireContext()),camplist)
                binding.mainlist.adapter = mainListadapter
                binding.mainlist.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.HORIZONTAL }
                // mainlist.layoutManager = GridLayoutManager(this@FragmentActivity, 2)  //>> 아이템리스트가 2줄로 나옴   | 아이템1 | 아이템2 |
                //LinearLayoutManager(this@FragmentActivity)//>> 수직
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //TODO("Not yet implemented")
                println("error : $t")
                println("error : ${call.request()}")
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("Life_cycle", "F onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d("Life_cycle", "F onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d("Life_cycle", "F onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d("Life_cycle", "F onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("Life_cycle", "F onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("Life_cycle", "F onDestroyView")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.d("Life_cycle", "F onDetach")
        super.onDetach()
    }

    //--------------------------------------------------------------------------------

    class MainListView1Adapter(
        var context: Context,
        val inflater1: LayoutInflater,
        var mCamplist: ArrayList<CampingList> = ArrayList<CampingList>()
    ) : RecyclerView.Adapter<MainListView1Adapter.ViewHolder>(){

        /*var camplist = arrayListOf<CampingList>(      //테스트 모델
            //CampingList(R.drawable.night_img, "다온캠핑장", "내 위치에서 1.24mi"),
            CampingList(R.drawable.camp1, "을왕리글램핑", "내 위치에서 0.3mi"),
            CampingList(R.drawable.camp2, "가평 포레스트카라반", "내 위치에서 1.5mi"),
            //CampingList(R.drawable.night_img, "녹천글램핑", "내 위치에서 1.0mi"),
            CampingList(R.drawable.camp1, "강릉카라반", "내 위치에서 0.5mi"),
            CampingList(R.drawable.camp2, "제주오토캠핑장", "내 위치에서 0.8mi"),
            CampingList(R.drawable.camp1, "주문진캠핑파크", "내 위치에서 1.1mi")
        )*/

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListView1Adapter.ViewHolder {
            val itemBinding = ActivityCampinglistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            //val view = inflater1.inflate(R.layout.activity_campinglist,parent, false)
            //return ViewHolder(view)
            return ViewHolder(itemBinding)
        }

        override fun getItemCount(): Int {
            //return camplist.size
            return mCamplist.size
        }

        override fun onBindViewHolder(holder: MainListView1Adapter.ViewHolder, position: Int) {
            /*holder.mainImg.setImageResource(Integer.parseInt(camplist.get(position).camp_img))
            holder.maintitle.setText(camplist.get(position).camp_name)
            holder.contenttext.setText(camplist.get(position).camp_content)*/
            //holder.mainImg.setImageResource(Integer.parseInt(mCamplist.get(position).jsonObject.get("camp_img").asString))
            holder.maintitle.setText(mCamplist.get(position).jsonObject.get("camp_name").asString)
            holder.contenttext.setText(mCamplist.get(position).jsonObject.get("camp_defaddr").asString)
        }

        /*inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val mainImg : ImageView
            val maintitle : TextView
            val contenttext : TextView

            init{
                mainImg = itemView.findViewById(R.id.mainimg)
                maintitle = itemView.findViewById(R.id.maintitle)
                contenttext = itemView.findViewById(R.id.contenttext)

                mainImg.setScaleType(ImageView.ScaleType.CENTER)

                itemView.setOnClickListener {
                    val position: Int = adapterPosition
                    val content = camplist.get(position).contenttext
                    val result = "result... OK"
                    *//*val intent = Intent(it.context, MainTapActivity::class.java).apply {
                        putExtra("imageID",position)
                        putExtra("content",content)
                        putExtra("result",result)
                    }
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*//*
                }
            }
        }*/
        inner class ViewHolder(itemView: ActivityCampinglistBinding) : RecyclerView.ViewHolder(itemView.root){
            val mainImg : ImageView
            val maintitle : TextView
            val contenttext : TextView

            init{
                mainImg = itemView.mainimg
                maintitle = itemView.maintitle
                contenttext = itemView.contenttext

                mainImg.setScaleType(ImageView.ScaleType.CENTER)

                itemView.root.setOnClickListener {
                    val position: Int = adapterPosition
                    val content = mCamplist.get(position).jsonObject.get("camp_content").asString
                    val result = "result... OK"
                    /* val intent = Intent(it.context, MainTapActivity::class.java).apply {
                         putExtra("imageID",position)
                         putExtra("content",content)
                         putExtra("result",result)
                     }
                     context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))*/
                }

            }
        }
    }
    //--------------------------------------------------------------------------------
}