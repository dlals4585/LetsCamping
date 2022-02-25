package com.letscamping.model

import com.google.gson.JsonObject
import java.io.Serializable

/*class CampingList(
    val camp_no:String,         //캠핑장번호(캠핑장 인덱스)
    val camp_name:String,       //캠핑장 이름
    val area_cd:String,         //지역구분코드
    val camp_newaddr:String,    //도로명주소
    val camp_defaddr:String,    //지번주소

    val camp_lat:String,        //위도
    val camp_long:String,       //경도
    val camp_content:String,    //설명
    val camp_img:String,        //캠핑장 이미지
    val camp_callno:String,     //전화번호

    /*val mainImg: Int,
    val maintitle: String,
    val contenttext: String*/
) { }*/
class CampingList(
    val jsonObject: JsonObject
)// : Serializable{ }

class CampRank (val cmp_name: String, val gender: String, val age: String, val photo: String)
