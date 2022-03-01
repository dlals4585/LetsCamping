package com.letscamping

import android.Manifest
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.letscamping.adapter.SelectListViewAdapter
import com.letscamping.application.RetrofitService
import com.letscamping.application.retrofitAPI
import com.letscamping.databinding.FragmentMyaddrBinding
import com.letscamping.model.CampingList
import com.letscamping.util.PermissionUtil
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import kotlinx.android.synthetic.main.fragment_myaddr.*

import java.util.*
import java.util.concurrent.TimeUnit

class MyAddrFragment : Fragment() {
    private lateinit var binding:FragmentMyaddrBinding
    var GPSlat: Double  = 37.5666805
    var GPSlng : Double  = 126.9784147
    private lateinit var lastKnownLocation : Location
    var getaddress1: String = ""
    var camplist : ArrayList<CampingList> = ArrayList<CampingList>()
    //private var isGPSEnabled : Boolean = false
    //private var isNetworkEnabled : Boolean = false
    private var location_request_code : Int = 0
    val addresspermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    lateinit var mPermission : PermissionUtil

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
        //return inflater.inflate(R.layout.fragment_myaddr,container , false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //onCreateView가 실행 된 다음 수행되는 주기
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        /*val bundle = arguments

        if(bundle != null){
            GPSlat = bundle.getDouble("GPSlat")
            GPSlng = bundle.getDouble("GPSlng")
        }*/

        mPermission = PermissionUtil(requireContext(),addresspermissions)
        mPermission.OnSetPermission()
        setAddressInfo()
        binding.myAddressMain.setOnClickListener{
            Toast.makeText(requireContext(),"myAddress_main_Click",Toast.LENGTH_LONG).show()
        }

        val address = binding.myAddressMain.text.toString().replace(" ","")
        getmyaddrscampsite(address)

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

    }
    fun getmyaddrscampsite(keyword:String){
        var totalcount = 0
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder().setLenient().create()  //MalformedJsonException 방지
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
                        totalcount = res?.get("totalCount")?.asInt!!.toInt()
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
                    } catch (e1: java.lang.Exception) {
                        println("Exception : $e1")
                    }
                }
                if(totalcount==0){
                    binding.selectList.visibility = View.GONE
                    binding.nullSelect.visibility = View.VISIBLE
                }else{
                    binding.selectList.visibility = View.VISIBLE
                    binding.nullSelect.visibility = View.GONE
                    val selectListadapter = SelectListViewAdapter(requireContext(), camplist)
                    binding.selectList.adapter = selectListadapter
                    binding.selectList.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.VERTICAL }
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
    @SuppressLint("SetTextI18n")
    private fun setAddressInfo(){
        try {
            myLocation()
            getAddress()
            val addrArray = getaddress1.split(" ")
            println("주소 ==== " + addrArray)
            binding.myAddressMain.text = "" + addrArray[1] + " " + addrArray[2]
        }catch(e : Exception){}
    }

    private fun getAddress() {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geoCoder.getFromLocation(GPSlat, GPSlng, 1).first().getAddressLine(0)
        getaddress1 = address.toString()
        println("position Address == $address")
    }

    fun refreshFragment(fragment: Fragment) {
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
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

