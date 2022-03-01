package com.letscamping

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.adapter.SelectListViewAdapter
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.ActivityCampinglist1Binding
import com.letscamping.databinding.ActivitySelectCampsiteBinding
import com.letscamping.model.CampingList
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class SelectCampsiteActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectCampsiteBinding
    var camplist : ArrayList<CampingList> = ArrayList<CampingList>()
    var keyword:String = ""
    var keyword1:String = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCampsiteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        keyword = intent.getStringExtra("keyword1").toString()
        try {
            keyword1 = intent.getStringExtra("keyword2").toString()
        }catch (e:Exception){
            println("keyword2 putExtra data is null")
        }

        binding.textview1.text = keyword
        println("text=${keyword}")
        getGoCamplist_keyword(keyword)

        if(keyword1 != ""){
            binding.textview1.text = "$keyword/$keyword1"
            getGoCamplist_keyword(keyword1)
        }
    }

    fun getGoCamplist_keyword(keyword:String) {
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
        params["pageNo"] = "1"
        params["keyword"] = keyword
        server.getSearchCamp("getSearchCamp",params).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){
                    try {
                        val res = response.body()
                        //println("res:${res.toString()}")
                        val totalcount = res?.get("totalCount")?.asInt!!.toInt()
                        //println("totalcount = $totalcount")
                        val items = res.getAsJsonObject("items").asJsonObject
                        if(totalcount<=1){
                            val item = items.get("item") as JsonObject
                            //println("items===  ${item.toString()}")
                            val camplist1: CampingList = CampingList(item)
                            camplist.add(camplist1)
                        }else{
                            val bodyArray = items?.get("item")?.asJsonArray ?: JsonArray()
                            val parser = JsonParser()
                            val jsonObj = parser.parse(items.toString()) as JsonObject
                            val memberArray = jsonObj["item"] as JsonArray

                            for (i in 0 until bodyArray.size()){
                                val data1 = memberArray[i] as JsonObject
                                val camplist1: CampingList = CampingList(data1)
                                camplist.add(camplist1)
                            }
                        }
                    } catch (e: JSONException) {
                        println("JSONException : $e")
                    } catch (e1: Exception) {
                        println("Exception : $e1")
                    }
                }
                for (a in camplist){
                    println("facltNm_data == > ${a.jsonObject.get("facltNm").toString()}")
                }
                val selectListadapter = SelectListViewAdapter(applicationContext, camplist)
                binding.selectList.adapter = selectListadapter
                binding.selectList.layoutManager = LinearLayoutManager(applicationContext).also { it.orientation = LinearLayoutManager.VERTICAL }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //TODO("Not yet implemented")
                println("error : $t")
                println("error : ${call.request()}")
            }
        })
    }

}