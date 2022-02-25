package com.letscamping.application

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.letscamping.model.AreaResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList




interface retrofitAPI {

    //post1
    //매개변수를 미리 정해두는 방식
    /*@POST("home")
    fun postRequest(
        @Field("area_no") area_no:String,
        @Field("area_Cd") area_Cd:String,
        @Field("area_Name") area_Name:String
    ): Call<ResponseDTO>*/

    //post2
    //호출하는 곳에서 매개변수를 HashMap 형대로 보내는 방식
    @POST("/{path}")
    fun addParamsToURL(
        @Path("path") path: String,
        @Body parameters: HashMap<String, Any>
    ): Call<String> //String? //Call<AreaInfo>


    //GET 예제
    @GET("/{path}")
    fun getArea(@Path("path") path: String): Call<JsonObject>

    @GET("/{path}/{pageNo}")
    fun getgoCamp(
        @Path("path") path: String,
        @Query("pageNo") parameter:String
    ): Call<JsonObject>

    @POST("/{path}")
    fun getSearchCamp(
        @Path("path") path: String,
        @Body parameters: HashMap<String, Any>
    ): Call<JsonObject>

    @GET("/{path}")
    fun getArea(
        @Path("path") path: String,
        @Query("area_no") areaNo: String,
        @Query("area_Cd") areaCd: String,
        @Query("area_Name") areaName: String,
    ): Call<JsonObject>  // String? //Call<AreaInfo>


    //POST 예제
    @FormUrlEncoded
    @POST("/{path}")
    fun getContactsObject(
        @Field("area_no") areaNo: String,
        @Field("area_Cd") areaCd: String,
        @Field("area_Name") areaName: String,
    ): Call<JsonObject>

}

/*fun addParamsToURL(
    url: String?,
    paramValues: Map<String?, String?>
): String? {
    var url = url
    val params: ArrayList<NameValuePair> = ArrayList<NameValuePair>()
    for (name in paramValues.keys) {
        params.add(BasicNameValuePair(name, paramValues[name]))
    }
    val paramString: String = URLEncodedUtils.format(params, "utf-8")
    url += "?$paramString"
    return url
}*/
