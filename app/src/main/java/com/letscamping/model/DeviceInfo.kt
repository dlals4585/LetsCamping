package com.letscamping.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

/**
 * 작성일 : 2022-02-02
 * 작성자 : 이현강
 * 내용 : 디바이스 정보
 **/

class DeviceInfo (val context: Context){

    @SuppressLint("HardwareIds")
    fun getDeviceId():String { // 단말기의 IMEI 정보
        return Settings.Secure.getString(context.contentResolver,Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceIMEI():String{
        val phonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        var id = phonyManager!!.deviceId //app의 targetSdkVersion을 22 이상 올리면 이 부분에서 오류가 생김
        if (id == null) {
            id = "not available"
        }
        return id
    }

    fun getDeviceModel():String {   //단말기 모델 정보
        return Build.ID
    }

    fun getDeviceOS():String {      //단말기 OS 정보
        return Build.VERSION.RELEASE.toString()
    }

    fun getDeviceAppVersion():String { //단말기가 다운로드 받은 현재 앱의 버전 정보
        val info : PackageInfo = context.packageManager.getPackageInfo(context.packageName,0)
        return info.versionName
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    fun getPhoneNum():String {
        var phoneNumber:String = ""
        val systemService = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (systemService.line1Number != null) {
            phoneNumber = phoneNumber.substring(
                phoneNumber.length - 10,
                phoneNumber.length
            )
            phoneNumber = "0$phoneNumber"
        }else{
            phoneNumber = "00000000000"
        }
        return phoneNumber
    }
}