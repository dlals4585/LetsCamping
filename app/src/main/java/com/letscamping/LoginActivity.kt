package com.letscamping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.letscamping.databinding.ActivityLoginpageBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
//class LoginActivity : Fragment() {
    private lateinit var binding: ActivityLoginpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.exitLoginpage.setOnClickListener {
            finish()
        }
        binding.emailLogin.setOnClickListener {
            Toast.makeText(this,"이메일 로그인",Toast.LENGTH_SHORT).show()
            //로그인 완료 시 이전 화면으로 돌아가며, DB에 로그인 시간을 갱신하여 내정보 페이지 정보를 새로고침 함.
            setDataAtFragment(MyInfoFragment(),true)
            finish()
        }
        binding.phonnoLogin.setOnClickListener { Toast.makeText(this,"휴대폰 로그인",Toast.LENGTH_SHORT).show() }
        binding.kakaoLogin.setOnClickListener { Toast.makeText(this,"카카오 로그인",Toast.LENGTH_SHORT).show() }
        binding.naverLogin.setOnClickListener { Toast.makeText(this,"네이버 로그인",Toast.LENGTH_SHORT).show() }
        binding.plusMember.setOnClickListener{ Toast.makeText(this,"회원가입",Toast.LENGTH_SHORT).show() }
    }
    fun setDataAtFragment(fragment: Fragment,data:Boolean){
        val bundle = Bundle()
        bundle.putBoolean("logininfo",data)
        fragment.arguments = bundle
        setFragment(fragment)
    }
    fun setFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null).detach(fragment).attach(fragment)
        transaction.setReorderingAllowed(false)//.replace(R.id.layout,fragment,fragment.javaClass.getSimpleName())
        transaction.commit()
        fragment.onStart()
    }
    /*fun setFragment(fragment: Fragment){
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null).detach(fragment).attach(fragment)
        transaction.setReorderingAllowed(false).replace(R.id.layout,fragment,fragment.javaClass.getSimpleName())
        transaction.commit()
    }*/

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Life_cycle", "F onCreateView")
        binding = ActivityLoginpageBinding.inflate(layoutInflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //onCreateView가 실행 된 다음 수행되는 주기
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding.exitLoginpage.setOnClickListener {
            //finish()
        }
        binding.emailLogin.setOnClickListener {
            Toast.makeText(activity,"이메일 로그인",Toast.LENGTH_SHORT).show()
            setDataAtFragment(MyInfoFragment(),true)
            *//*val intent = Intent(this,MyInfoFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            tartActivity(intent)*//*
        }
        binding.phonnoLogin.setOnClickListener { Toast.makeText(activity,"휴대폰 로그인",Toast.LENGTH_SHORT).show() }
        binding.kakaoLogin.setOnClickListener { Toast.makeText(activity,"카카오 로그인",Toast.LENGTH_SHORT).show() }
        binding.naverLogin.setOnClickListener { Toast.makeText(activity,"네이버 로그인",Toast.LENGTH_SHORT).show() }
        binding.plusMember.setOnClickListener{ Toast.makeText(activity,"회원가입",Toast.LENGTH_SHORT).show() }
    }*/

    /*fun kakaoLogin(){
        try{
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    when {
                        error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.ServerError.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                        }
                        error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                            Toast.makeText(requireContext().applicationContext, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                        }
                        else -> { // Unknown
                            Toast.makeText(requireContext().applicationContext, "기타 에러", Toast.LENGTH_SHORT).show()
                        }
                        //Toast.makeText(requireContext().applicationContext,"카카오계정으로 로그인 실패",Toast.LENGTH_LONG).show()
                        //Log.e(TAG, "카카오계정으로 로그인 실패", error)
                    }
                } else if (token != null) {
                    Toast.makeText(requireContext().applicationContext,"카카오계정으로 로그인 성공 ${token.accessToken}",Toast.LENGTH_LONG).show()
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Toast.makeText(requireContext().applicationContext,"카카오톡으로 로그인 실패",Toast.LENGTH_LONG).show()
                        //Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                    } else if (token != null) {
                        Toast.makeText(requireContext().applicationContext,"카카오톡으로 로그인 성공 ${token.accessToken}",Toast.LENGTH_LONG).show()
                        //Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }catch (e: Exception){
            println("kakao error : "+e)
        }
    }
    fun kakaoLogOut(){
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(requireContext().applicationContext,"로그아웃 실패. SDK에서 토큰 삭제됨",Toast.LENGTH_LONG).show()
                //Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            }
            else {
                Toast.makeText(requireContext().applicationContext,"로그아웃 성공. SDK에서 토큰 삭제됨",Toast.LENGTH_LONG).show()
                //Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }*/

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
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
    }*/
    fun saveInfo(userId:String,usetPw:String){}
}