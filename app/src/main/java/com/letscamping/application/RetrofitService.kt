package com.letscamping.application

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Path




class RetrofitService {
    private val TAG = this::class.java.simpleName
    private val API_KEY = "sample"


    private var retrofitService:retrofitAPI ?= null
    private val client: OkHttpClient
    //val DEFAULT_URL = "http://192.168.45.200:9090/"    //서버 URL
    val DEFAULT_URL = "http://192.168.75.36:9090/"    //서버 URL

    //DEFAULT_URL+"home"
    init {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    fun createRetrofitApi() {
        var gson = GsonBuilder().setLenient().create()  //MalformedJsonException 방지
        retrofitService = Retrofit.Builder()
            .baseUrl(DEFAULT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build() .create(retrofitAPI::class.java) //자바코드의 class 함수 사용 방법
    }

    /*fun getContentList(
        success: (List<ContentModel>) -> Unit,
        error: (Call<ContentResult>, Throwable) -> Unit
    ) {
        val call = retrofitService!!.getContent(API_KEY)

        //enqueue: Queue 에 삽입한다
        call.enqueue(object :
            Callback<ContentResult> {
            override fun onResponse(
                call: Call<ContentResult>,
                response: Response<ContentResult>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.Content //?로 null체크

                    // Log.d(TAG, "Parsed: $data")
                    data?.let { success(data) } //null이 아닌 경우 실행
                } else {
                    Log.d(TAG, "${response.body()?.toString()}")
                }
            }

            override fun onFailure(call: Call<ContentResult>, t: Throwable?) {
                Log.d(TAG, "실패 : {$t}")
                call.let {
                    if (t != null) {
                        error(it, t)
                    }
                }
            }
        })
    }*/
}
abstract class ResponseData {
    val message: String? = null
    val code: String? = null
    val error_message: String? = null
}
data class ContentResult (
    @SerializedName("Content")
    val content: List<ContentModel> = emptyList()
) : ResponseData()

data class ContentModel(
    //변수명과 json key 값을 다르게 사용하고 싶은 경우 @SerializedName annotation 사용
    @SerializedName("author")
    val author: String = "",
    @SerializedName("category")
    val category: String = "",
)
