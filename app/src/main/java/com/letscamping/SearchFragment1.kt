package com.letscamping

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.ActivityArealistBinding
import com.letscamping.databinding.FragmentSearch1Binding
import com.letscamping.model.AreaInfo
//import com.letscamping.model.AreaList
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchFragment1 : Fragment() {

    //var mAreaName : ArrayList<AreaList> = ArrayList<AreaList>()
    val marealist1 = arrayListOf<AreaInfo>()
    //val arealist = ArrayList<AreaInfo>()

    private lateinit var binding:FragmentSearch1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
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
        binding = FragmentSearch1Binding.inflate(layoutInflater)
        val view = binding.root
        //val view = inflater.inflate(R.layout.fragment_search1,container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //onCreateView가 실행 된 다음 수행되는 주기(Fragment에서 액티비티에 대한 이벤트나 세팅은 여기에서 한다.)
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        getArea()
    }

    fun getArea(){
        val mAreaName1 : ArrayList<AreaInfo> = ArrayList<AreaInfo>()
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
        server.getArea("area").enqueue(object : Callback<JsonObject> {
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
                            //println("자역명 : ${data1.get("area_Name")}")
                            /*val areaInfo = AreaInfo(data1)
                            arealist.add(areaInfo)*/
                            val arealist:AreaInfo = AreaInfo(data1.get("area_no").asInt, data1.get("area_Cd").asString, data1.get("area_Name").asString)
                            mAreaName1.add(arealist)
                            //marealist1.add(arealist)
                        }
                    } catch (e: JSONException) {
                        println("JSONException : $e")
                    }
                }

                val areaListadapter = AreaListViewAdapter(requireContext(), LayoutInflater.from(requireContext()), mAreaName1)
                binding.areaList.adapter = areaListadapter
                binding.areaList.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.VERTICAL }
                // mainlist.layoutManager = GridLayoutManager(this@FragmentActivity, 2)  //>> 아이템리스트가 2줄로 나옴   | 아이템1 | 아이템2 |
                //LinearLayoutManager(this@FragmentActivity)//>> 수직
                /*for (a in mAreaName1){
                    println("data == > ${a.area_Cd}, ${a.area_Name}")
                }*/
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
    class AreaListViewAdapter(
        var context: Context,
        val inflater1: LayoutInflater,
        var marealist1: ArrayList<AreaInfo> = ArrayList<AreaInfo>()
    ) : RecyclerView.Adapter<AreaListViewAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaListViewAdapter.ViewHolder {
            val itemBinding = ActivityArealistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(itemBinding)
        }

        override fun getItemCount(): Int {
            return marealist1.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: AreaListViewAdapter.ViewHolder, position: Int) {
            holder.area_No.text = (position + 1).toString()
            holder.area_Name.text = marealist1.get(position).area_Name
            holder.area_Cd.text = marealist1.get(position).area_Cd
        }

        inner class ViewHolder(itemView: ActivityArealistBinding) : RecyclerView.ViewHolder(itemView.root){
            val area_No : TextView = itemView.areaNo
            val area_Name : TextView = itemView.areaName
            val area_Cd : TextView = itemView.areaCd
            init{
                area_Cd.visibility = View.GONE
                itemView.root.setOnClickListener {
                    //Toast.makeText(context,"${position+1} 번째 아이템",Toast.LENGTH_SHORT).show()
                    val position: Int = adapterPosition
                    val content = marealist1.get(position).area_Cd
                    val result = "result... OK"
                    val intent = Intent(it.context, MainTapActivity::class.java).apply {
                         putExtra("imageID",position)
                         putExtra("content",content)
                         putExtra("result",result)
                     }
                     context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
        }
    }
    //--------------------------------------------------------------------------------
}
