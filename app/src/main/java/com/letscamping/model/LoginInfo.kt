package com.letscamping.model

import android.os.Parcel
import android.os.Parcelable

import org.json.JSONException
import org.json.JSONObject

/**
 * 작성일 : 2022-02-02
 * 작성자 : 이현강
 * 내용 : 로그인
 **/

class LoginInfo() : Parcelable {

    private var Kakao_id = ""
    private var Naver_id = ""
    private var Email = ""
    private var UserPw = ""
    private var Phone_no = ""


    constructor(parcel: Parcel) : this() {
        this.Email = parcel.readString().toString()
        this.UserPw = parcel.readString().toString()
        this.Phone_no = parcel.readString().toString()
        this.Kakao_id = parcel.readString().toString()
        this.Naver_id = parcel.readString().toString()
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        //TODO("Not yet implemented")
        dest?.writeString(this.Email)
        dest?.writeString(this.UserPw)
        dest?.writeString(this.Phone_no)
        dest?.writeString(this.Kakao_id)
        dest?.writeString(this.Naver_id)
    }

    companion object CREATOR : Parcelable.Creator<LoginInfo> {
        override fun createFromParcel(parcel: Parcel): LoginInfo {
            return LoginInfo(parcel)
        }

        override fun newArray(size: Int): Array<LoginInfo?> {
            return arrayOfNulls(size)
        }
    }

    @Throws(JSONException::class)
    constructor(jsonObject: JSONObject) : this() {
        /*if (!jsonObject.isNull("Email")) this.setEmail(jsonObject.getString("Email"))
        if (!jsonObject.isNull("UserPw")) this.setUserPw(jsonObject.getString("UserPw"))
        if (!jsonObject.isNull("Phone_no")) this.setPhone_no(jsonObject.getString("Phone_no"))
        if (!jsonObject.isNull("Kakao_id")) this.setKakao_id(jsonObject.getString("Kakao_id"))
        if (!jsonObject.isNull("Naver_id")) this.setNaver_id(jsonObject.getString("Naver_id"))*/
        if (!jsonObject.isNull("Email")) this.Email = jsonObject.getString("Email")
        if (!jsonObject.isNull("UserPw")) this.UserPw = jsonObject.getString("UserPw")
        if (!jsonObject.isNull("Phone_no")) this.Phone_no = jsonObject.getString("Phone_no")
        if (!jsonObject.isNull("Kakao_id")) this.Kakao_id = jsonObject.getString("Kakao_id")
        if (!jsonObject.isNull("Naver_id")) this.Naver_id = jsonObject.getString("Naver_id")
    }

}