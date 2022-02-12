package com.letscamping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.letscamping.databinding.FragmentReservationBinding

class MyReservationFragment : Fragment() {
    private lateinit var binding:FragmentReservationBinding
    /*override fun onAttach(context: Context?) {
        Log.d("Life_cycle", "F onAttach")
        super.onAttach(context)
    }*/

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
        binding = FragmentReservationBinding.inflate(layoutInflater)
        //val view = inflater.inflate(R.layout.fragment_reservation,container , false)
        val view = binding.root
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //onCreateView가 실행 된 다음 수행되는 주기
        Log.d("Life_cycle", "F onViewCreated")
        super.onViewCreated(view, savedInstanceState)
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
