package com.letscamping

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.letscamping.databinding.ActivityOpenMapBinding
//import kotlinx.android.synthetic.main.activity_open_map.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class OpenKakaoMap : AppCompatActivity() {
    private lateinit var binding: ActivityOpenMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenMapBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_open_map)
        setContentView(view)
        val GPSlat = intent.getDoubleExtra("GPSlat",0.0)
        val GPSlng = intent.getDoubleExtra("GPSlng",0.0)
        println("position2 == $GPSlat, $GPSlng")
        printHashKey()

        var mapView = MapView(this@OpenKakaoMap)
        binding.mapView.addView(mapView)
        //mapView.setCalloutBalloonAdapter(CustomCalloutBalloonAdapter())
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(GPSlat, GPSlng), true)
        mapView.setZoomLevel(1, true)
        mapView.zoomIn(true)// 줌 인
        mapView.zoomOut(true)// 줌 아웃

        val marker = MapPOIItem()
        marker.itemName = ""
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(GPSlat, GPSlng)
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker)
    }

    ////해쉬키를 구하는 코드 (배포용 = APK 생성 후 연결해서 Log 확인),(디버그용 = 디버그 연결 상태에서 Log 확인)
    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA1")
                md.update(signature.toByteArray())
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

}
