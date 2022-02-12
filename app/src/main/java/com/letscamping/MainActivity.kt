package com.letscamping

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.letscamping.databinding.ActivityMainBinding
import com.letscamping.model.DeviceInfo
import com.letscamping.util.PermissionUtil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private val TAG = javaClass.simpleName
    private var main_request_code : Int = 0
    val mainpermissions = arrayOf(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Manifest.permission.READ_PHONE_NUMBERS
        } else {
            Manifest.permission.READ_PHONE_STATE
        },
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    lateinit var mPermission : PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_main)
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW))
        }

        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                //Log.d(TAG, "Key: $key Value: $value")
                println("Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]

        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                println("subscribe_topics : "+msg)
                //Log.d(TAG, msg)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]

        // Get token
        // [START log_reg_token]
        Firebase.messaging.getToken().addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                println("Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            //Log.d(TAG, msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            println(msg)
        })
        // [END log_reg_token]
        //Toast.makeText(this, "See README for setup instructions", Toast.LENGTH_SHORT).show()

        mPermission = PermissionUtil(this,mainpermissions)
        if(mPermission.OnSetPermission()){
            Applicationstart()
        }
    }
    companion object {
        private const val DURATION : Long = 2000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun Applicationstart(){
        if (isConnectingToInternet(this)) {
            Handler().postDelayed({
                val intent = Intent(this, MainTapActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        } else {
            AlertDialog.Builder(this)
                .setTitle("렛츠캠핑")
                .setMessage("데이터 사용 연결이 되어있지 않습니다 연결 후 사용해주시기 바랍니다.")
                .setPositiveButton("확인") { dialog: DialogInterface?, which: Int ->
                    dialog?.dismiss()
                    finish()
                }
                .show()
        }
    }
    // 시작과 동시에 네트워크 연결확인을 한다
    open fun isConnectingToInternet(
        _context:Context
    ): Boolean {
        //if(chkPermission()){

        val connectivity = _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val mobile = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val blutooth = connectivity.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH)
            // 3G, LTE 연결
            if (mobile != null) {
                if (mobile.isConnected) {
                    return true
                }
            }
            // WIFI 연결
            if (wifi != null) {
                if (wifi.isConnected) {
                    return true
                }
            }
            // blutooth 연결
            if(blutooth!=null){
                if (blutooth.isConnected){
                    return true
                }
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
    }

    //--------------------------------------------------------------------------------------------------------
    // 사용자가 권한 요청<허용,비허용>한 후에 이 메소드가 호출됨
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === main_request_code) {
            // 권한을 모두 승인했는지 여부
            if (!mPermission.verifyPermission(permissions, grantResults)) { // 권한 얻기 실패
                Log.d("Tag", "MainActivity_권한얻기 실패")
                Applicationstart()
            } else {
                Log.d("Tag", "MainActivity_권한얻기 성공")
                val deviceInformation = DeviceInfo(this)
                try {
                    Log.d("Tag", "deviceInformation getDeviceId 확인 :" + deviceInformation.getDeviceId())
                    Log.d("Tag","deviceInformation getDeviceId 확인 :" + deviceInformation.getDeviceIMEI())
                    Log.d("Tag", "deviceInformation getPhoneNum 확인 :" + deviceInformation.getPhoneNum())
                } catch (e: Exception) {
                    Log.d("Tag", "deviceInformation 에러확인 : $e")
                }
                Applicationstart()
            }
        }
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("test1234")
        if (requestCode === location_request_code) {
            // 권한을 모두 승인했는지 여부
            var chkFlag = false
            // 승인한 권한은 0 값, 승인 안한 권한은 -1을 값으로 가진다.
            for (g in grantResults) {
                if (g == -1) {
                    chkFlag = true
                    break
                }
            }
            // 권한 중 한 개라도 승인 안 한 경우
            if (chkFlag) {
                PermissionUtil(this).chkPermission()
            }
        }
    }*/
}
