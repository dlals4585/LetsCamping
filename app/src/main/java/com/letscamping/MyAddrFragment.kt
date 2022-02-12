package com.letscamping

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.gson.*
import com.google.gson.internal.Streams.parse
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.FragmentMyaddrBinding
import com.letscamping.model.AreaInfo
import com.letscamping.util.PermissionUtil
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
//import kotlinx.android.synthetic.main.fragment_myaddr.*

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import org.json.JSONException
import com.google.gson.JsonArray

import com.google.gson.JsonObject

import com.google.gson.JsonParser
import android.R.attr.data









class MyAddrFragment : Fragment() {
    private lateinit var binding:FragmentMyaddrBinding
    var GPSlat: Double  = 37.5666805
    var GPSlng : Double  = 126.9784147
    private lateinit var lastKnownLocation : Location
    var getaddress1: String = ""
    //private var isGPSEnabled : Boolean = false
    //private var isNetworkEnabled : Boolean = false
    private var location_request_code : Int = 0
    val addresspermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    lateinit var mPermission : PermissionUtil

    val mAreaInfo : ArrayList<AreaInfo> = ArrayList<AreaInfo>()
    override fun onAttach(context: Context) {
        Log.d("Life_cycle", "F onAttach")
        super.onAttach(context)
        /*if (context != null) {
            super.onAttach(context)
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?){
        Log.d("Life_cycle", "F onCreate")
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
        binding = FragmentMyaddrBinding.inflate(layoutInflater)
        val view = binding.root
        //return inflater.inflate(R.layout.fragment_myaddr,container , false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //onCreateView가 실행 된 다음 수행되는 주기
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        mPermission = PermissionUtil(requireContext(),addresspermissions)
        mPermission.OnSetPermission()
        setAddressInfo()
        binding.myAddressMain.setOnClickListener{
            Toast.makeText(requireContext(),"myAddress_main_Click",Toast.LENGTH_LONG).show()
        }
        binding.openMap.setOnClickListener {
            var intent1 = Intent(requireContext(), OpenKakaoMap::class.java)
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent1.putExtra("GPSlat", GPSlat)
            intent1.putExtra("GPSlng", GPSlng)
            requireContext().startActivity(intent1)
        }
        binding.selectdate.setOnClickListener {
            var intent2 = Intent(requireContext(),CalendarActivity::class.java)
            requireContext().startActivity(intent2)
        }

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
            .build()

        val server: retrofitAPI = retrofit.create(retrofitAPI::class.java)

        val params:HashMap<String,Any> = HashMap<String,Any>()
        //params.put()
        //server.addParamsToURL("home",params).enqueue(object : Callback<AreaInfo>{
        server.getArea("area").enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //TODO("Not yet implemented")
                if(response.isSuccessful){
                    val res = response.body()
                    val bodyArray = res?.get("data")?.asJsonArray ?: JsonArray()

                    val parser = JsonParser()
                    val jsonObj = parser.parse(res.toString()) as JsonObject
                    val memberArray = jsonObj["data"] as JsonArray
                    //val areainfo = JSONObject(res.toString()).getString("data")

                    val ninfo = ArrayList<AreaInfo>()
                    try {
                        for (i in 0 until bodyArray.size()){
                            val data1 = memberArray[i] as JsonObject

                            println("자역명 : ${data1.get("area_Name")}")
                        }

                    } catch (e: JSONException) {
                        println("JSONException : $e")
                    }
                }
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

    @SuppressLint("MissingPermission")
    fun myLocation(){
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // GPS 프로바이더 사용가능여부
        var isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 네트워크 프로바이더 사용가능여부
        var isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    GPSlat = it.latitude
                    GPSlng = it.longitude
                    //val position = LatLng(it.latitude, it.longitude)
                    println("position == "+GPSlat+ ", " + GPSlng)
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        when{
            isNetworkEnabled -> {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            isGPSEnabled -> {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            else -> {}
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 1f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 1f, locationListener)

        // 수동으로 위치 구하기
        val locationProvider = LocationManager.GPS_PROVIDER
        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider)
        if (lastKnownLocation != null) {
            GPSlng = lastKnownLocation.longitude
            GPSlat = lastKnownLocation.latitude
        }
    }
    private fun setAddressInfo(){
        try {
            myLocation()
            getAddress()
            var addrArray = getaddress1.split(" ")
            println("주소 ==== " + addrArray)
            binding.myAddressMain.text = "" + addrArray[1] + " " + addrArray[2]
        }catch(e : Exception){}
    }
    private fun getAddress() {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCoder.getFromLocation(GPSlat, GPSlng, 1).first().getAddressLine(0)
        getaddress1 = address.toString()
        println("position Address == "+address.toString())
    }

    fun refreshFragment(fragment: Fragment) {
        var ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("requestCode=="+requestCode)
        println("location_request_code=="+location_request_code)
        if (requestCode === location_request_code) {
            // 권한을 모두 승인했는지 여부
            if (!mPermission.verifyPermission(permissions, grantResults)) { // 권한 얻기 실패
                /*val intent = (context as Activity).getIntent()
                (context as Activity).finish() //현재 액티비티 종료 실시
                (context as Activity).overridePendingTransition(0, 0) //효과 없애기
                (context as Activity).startActivity(intent) //현재 액티비티 재실행 실시
                (context as Activity).overridePendingTransition(0, 0) //효과 없애기*/
                refreshFragment(this)
            } else {
                setAddressInfo()
                refreshFragment(this)
             /*   var intent2 : Intent  = getIntent();
                finish();
                startActivity(intent2);*/
            }

        }
    }

}

