package com.letscamping

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.letscamping.adapter.CalendarAdapter
import com.letscamping.databinding.FragmentCalendarBinding
/*import kotlinx.android.synthetic.main.banner_list_item.view.*
import kotlinx.android.synthetic.main.calendar_item.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_carousel.**/
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    //private lateinit var binding: BannerListItemBinding
    //private lateinit var binding1: CalendarItemBinding
    private lateinit var binding: FragmentCalendarBinding
    //private lateinit var binding3: FragmentCarouselBinding
//class CalendarFragment : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    lateinit var mContext: Context
    lateinit var currentDate: Date

    lateinit var calendarAdapter: CalendarAdapter
    var pageIndex = Int.MAX_VALUE / 2
    var datetime: String? = ""
    companion object {
        var instance: CalendarFragment? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        val view = binding.root
        //val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*binding.calendarViewPager.adapter = ViewPagerAdapter(getBannerList())
        binding.calendarViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.calendarViewPager.setCurrentItem(pageIndex, false)*///임시 주석

        /*calendarViewPager.apply {
            //firstFragmentStateAdapter.setCurrentItem(this.firstFragmentPosition, false)
        }*/
        /*linear_layout_see_all.setOnClickListener {
            Toast.makeText(requireContext(), "해당 날짜 선택", Toast.LENGTH_LONG).show()
        }*/
        //calendar_year_month_text.text = datetime
    }
    fun initView(view: View) {
        println("Int.MAX_VALUE == "+Int.MAX_VALUE)
        pageIndex -= (Int.MAX_VALUE / 2)
        Log.i(TAG, "Calender Index: $pageIndex")
        // 날짜 적용
        val date = Calendar.getInstance()
        // 현재시간을 가져오기
        val long_now = System.currentTimeMillis()
        // 현재 시간을 Date 타입으로 변환
        val t_date = Date(long_now)

        println("Calendar.MONTH=="+Calendar.MONTH)
        currentDate = date.run {
            add(Calendar.MONTH, pageIndex)
            time
        }
        Log.i(TAG+"date==", "$date")
        // 포맷 적용
        //var datetime: String = SimpleDateFormat(mContext.getString(R.string.calendar_year_month_format),Locale.KOREA).format(date.time)
        datetime = SimpleDateFormat("yyyy년 MM월",Locale.KOREA).format(currentDate.time)
        //var datetime: String? = SimpleDateFormat("yyyy년 MM월",Locale.KOREA).format(date.time)
        println("date.time=="+datetime)
    }


    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    private fun getBannerList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3)
    }

    class ViewPagerAdapter(
        bannerList: ArrayList<Int>
    ) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
        var item = bannerList
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

        override fun getItemCount(): Int = Int.MAX_VALUE	// 아이템의 갯수를 임의로 확 늘린다.

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            /*holder.banner.setScaleType(ImageView.ScaleType.CENTER)
            holder.banner.setImageResource(item[position%3])	// position에 3을 나눈 나머지 값을 사용한다.*/
        }
        inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
            (LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)){
            //val banner = itemView.imageView_banner!!
        }

        /*override fun createFragment(position: Int): Fragment {
            val calendarFragment = CalendarFragment()
            calendarFragment.pageIndex = position
            return calendarFragment
        }*/
    }
}


