package com.letscamping.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.letscamping.application.ContentModel
import com.letscamping.application.ResponseData

import org.json.JSONException
import org.json.JSONObject

/**
 * 작성일 : 2022-02-02
 * 작성자 : 이현강
 * 내용 : 지역번호 / 지역명
 **/


/*class AreaInfo() : Parcelable {

    private var area_no = ""
    private var area_Cd = ""
    private var area_Name = ""

    constructor(parcel: Parcel) : this() {
        this.area_no = parcel.readInt().toString()
        this.area_Cd = parcel.readString().toString()
        this.area_Name = parcel.readString().toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        //TODO("Not yet implemented")
        dest?.writeString(this.area_no)
        dest?.writeString(this.area_Cd)
        dest?.writeString(this.area_Name)
    }

    companion object CREATOR : Parcelable.Creator<AreaInfo> {
        override fun createFromParcel(parcel: Parcel): AreaInfo {
            return AreaInfo(parcel)
        }

        override fun newArray(size: Int): Array<AreaInfo?> {
            return arrayOfNulls(size)
        }
    }

    @Throws(JSONException::class)
    constructor(jsonObject: JsonObject) : this() {
        area_no = jsonObject.get("area_no").toString()
        area_Cd = jsonObject.get("area_Cd").toString()
        area_Name = jsonObject.get("area_Name").toString()
    }

    @Throws(JSONException::class)
    fun AreaInfo(jsonObject: JSONObject) {
        if (!jsonObject.isNull("area_no")) area_no = jsonObject.getString("area_no")
        if (!jsonObject.isNull("area_Cd")) area_Cd = jsonObject.getString("area_Cd")
        if (!jsonObject.isNull("area_Name")) area_Name = jsonObject.getString("area_Name")
    }

}*/

class AreaInfo(
    val area_no: Int,
    val area_Cd: String,
    val area_Name: String
) { }

data class AreaResponse(
    val mAreaInfo : List<AreaInfo>
)
/*data class AreaInfo(
    @SerializedName("area_no")
    val areaNo:Int,
    @SerializedName("area_Cd")
    val areaCd:String,
    @SerializedName("area_Name")
    val areaName:String,
)*/
// @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
// @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑
/*위처럼 JSON 데이터 속성명, 변수명 + 타입(String, Boolean) 일치필수
JSON - @SerializedName("속성명")으로 속성명 일치시켜주면 변수명 다르게 사용 가능
XML - @Element(name="속성명")으로 XML은 @Element 애노테이션 사용*/
