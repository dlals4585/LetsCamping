package com.letscamping

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.letscamping.databinding.FragmentMyinfoBinding

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.lang.Exception


//import kotlinx.android.synthetic.main.fragment_myinfo.*

class MyInfoFragment : Fragment() {
    private lateinit var binding: FragmentMyinfoBinding
    /*override fun onAttach(context: Context?) {
        Log.d("Life_cycle", "F onAttach")
        super.onAttach(context)
    }*/

    override fun onCreate(savedInstanceState: Bundle?){
        Log.d("Life_cycle", "F onCreate")
        super.onCreate(savedInstanceState)
    }

    fun setFragment(fragment: Fragment){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null).detach(fragment).attach(fragment)
        transaction.setReorderingAllowed(false)//.replace(R.id.layout,fragment,fragment.javaClass.getSimpleName())
        transaction.commit()
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
        binding = FragmentMyinfoBinding.inflate(layoutInflater)
        //val view = inflater.inflate(R.layout.fragment_myinfo,container , false)
        val view = binding.root
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //onCreateView가 실행 된 다음 수행되는 주기
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding.versionCd.setText(getAppVersion(requireContext()))
        val bundle = arguments
        var logininfo = false

        if(bundle != null){
            logininfo = bundle.getBoolean("logininfo")
        }

        println("logininfo = "+logininfo)
        if(logininfo){
            binding.loginTrue.visibility = View.VISIBLE
            binding.loginTF.visibility = View.VISIBLE
            binding.logout.visibility = View.VISIBLE
            binding.loginFalse.visibility = View.GONE
        }else{
            //View.INVISIBLE//View.GONE
            binding.loginTrue.visibility = View.GONE
            binding.loginTF.visibility = View.GONE
            binding.logout.visibility = View.GONE
            binding.loginFalse.visibility = View.VISIBLE
        }

        binding.login.setOnClickListener {
            startActivity(Intent(it.context, LoginActivity::class.java))//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            binding.loginTrue.visibility = View.VISIBLE
            binding.loginTF.visibility = View.VISIBLE
            binding.logout.visibility = View.VISIBLE
            binding.loginFalse.visibility = View.GONE
        }

        binding.logout.setOnClickListener{
            //View.INVISIBLE//View.GONE
            binding.loginTrue.visibility = View.GONE
            binding.loginTF.visibility = View.GONE
            binding.logout.visibility = View.GONE
            binding.loginFalse.visibility = View.VISIBLE
        }

        binding.permissionSetting.setOnClickListener {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                //val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS)
                intent.setData(Uri.parse("package:"+requireContext().packageName))
                startActivityForResult(intent, 0)
            }catch (e : ActivityNotFoundException){
                Toast.makeText(requireContext().applicationContext,"미지원 단말기",Toast.LENGTH_LONG).show()
            }

        }
        binding.infoNotice.setOnClickListener {
            startActivity(Intent(it.context, NoticeActivity::class.java))//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun getAppVersion(context: Context):String?{
        var versionName = ""
        val pi : PackageInfo = context.packageManager.getPackageInfo(context.packageName,0)
        versionName = pi.versionName
        return versionName
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
}
