package com.letscamping.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.content.pm.PermissionInfo
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created 2021-12-11.
 */

class PermissionUtil {

    var PERMISSIONS_REQUEST_CODE : Int = 0
    var mActivity: Context? = null
    var mstrPermissions: Array<String>
    var denyPermissions: ArrayList<String>? = null

    constructor(context: Context, i_Permissions: Array<String>) {
        this.mActivity = context
        this.mstrPermissions = i_Permissions
        //OnSetPermission()
    }

    fun OnSetPermission() : Boolean {
        var result : Boolean = false
        val arrayList = ArrayList<String>()
        for (s in mstrPermissions){
            if(chkPermission(s) == false){
                arrayList.add(s)
            }
        }
        if(arrayList.size > 0){
            OnRequestPermission(arrayList)
            result = false
        }else{
            result = true
        }
        return result
    }

    val camerapermissions = arrayOf(
        Manifest.permission.CAMERA
    )

    fun chkPermission(permission:String) : Boolean {
        var result2 : Boolean = false
        // 앱에 필요한 권한이 없을 때
        // <앱을 처음 실행하거나, 사용자가 이전에 권한 허용을 안 했을 때 성립>
        //휴대폰 정보, 문자메시지 퍼미션, 인터넷 퍼미션
        if (ContextCompat.checkSelfPermission(this.mActivity!!, permission) != PackageManager.PERMISSION_GRANTED ) {  // <PERMISSION_DENIED가 반환됨>
            result2 = false
        }else { result2 = true }
        return result2
    }
    private fun OnRequestPermission(i_arPermissions: ArrayList<String>) {
        var saRequestPermisstions: Array<String?>?
        saRequestPermisstions = arrayOfNulls(i_arPermissions.size)
        saRequestPermisstions = i_arPermissions.toArray<String>(saRequestPermisstions)

        // 사용자에게 권한 요청 팝업 발생
        ActivityCompat.requestPermissions((mActivity as Activity?)!!, saRequestPermisstions, PERMISSIONS_REQUEST_CODE)
    }

    // 사용자가 이전에 권한을 거부했을 때 호출된다.
    open fun verifyPermission( permissions: Array<out String>, grantresults: IntArray): Boolean {
        var i: Int
        var bRet = true
        if (grantresults.size < 1) {
            return false
        }
        denyPermissions = java.util.ArrayList()
        i = 0
        while (i < grantresults.size) {
            if (grantresults[i] != PackageManager.PERMISSION_GRANTED) {
                denyPermissions!!.add(permissions[i])
                bRet = false
            }
            i++
        }
        return bRet
    }

    fun showRequestAgainDialog() {
        val pm = mActivity!!.packageManager
        var pInfo: PermissionInfo
        var pGroupInfo: PermissionGroupInfo

        val strPermission = java.util.ArrayList<String>()
        var name: String

        try {
            for (s in denyPermissions!!) {
                pInfo = pm.getPermissionInfo(s, PackageManager.GET_META_DATA)
                pGroupInfo = pm.getPermissionGroupInfo(pInfo.group, PackageManager.GET_META_DATA)
                name = pGroupInfo.loadLabel(pm).toString()
                if (name == "android.permission-group.UNDEFINED") {
                    name = pInfo.loadLabel(pm).toString()
                }
                if (strPermission.indexOf(name) < 0) {
                    strPermission.add(name)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val builder = AlertDialog.Builder(mActivity)
        builder.setMessage("$strPermission 권한은 어플리케이션 동작에 꼭 필요함으로, 설정>권한 에서 활성화 하시기 바랍니다.")
            .setPositiveButton(
                "설정"
            ) { dialogInterface, i ->
                try {
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + mActivity!!.packageName))
                    mActivity!!.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    val intent =
                        Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                    mActivity!!.startActivity(intent)
                }
                ActivityCompat.finishAffinity((this.mActivity as Activity?)!!)
                System.exit(0)
            }
            .setNegativeButton( "취소" ) { dialogInterface, i ->
                ActivityCompat.finishAffinity((this.mActivity as Activity?)!!)
                System.exit(0)
            }
            .setCancelable(false)
        builder.show()
    }
}
