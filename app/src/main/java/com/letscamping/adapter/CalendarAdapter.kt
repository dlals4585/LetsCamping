package com.letscamping.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.letscamping.model.FurangCalendar
import com.letscamping.R
import com.letscamping.databinding.ListItemCalendarBinding
//import kotlinx.android.synthetic.main.list_item_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*

// 높이를 구하는데 필요한 LinearLayout과 FurangCalender를 사용할 때 필요한 date를 받는다.
class CalendarAdapter(val context: Context, val calendarLayout: LinearLayout, val date: Date) :
    RecyclerView.Adapter<CalendarAdapter.CalendarItemHolder>() {

    //private lateinit var binding: ListItemCalendarBinding
    private val TAG = javaClass.simpleName
    var dataList: ArrayList<Int> = arrayListOf()

    // FurangCalendar을 이용하여 날짜 리스트 세팅
    var furangCalendar: FurangCalendar = FurangCalendar(date)
    init {
        furangCalendar.initBaseCalendar()
        dataList = furangCalendar.dateList
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CalendarItemHolder, position: Int) {

        // list_item_calendar 높이 지정
        val h = calendarLayout.height / 6
        holder.itemView.layoutParams.height = h

        holder?.bind(dataList[position], position, context)
        if (itemClick != null) {
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemHolder {

        //val view = LayoutInflater.from(context).inflate(R.layout.list_item_calendar, parent, false)
        val itemBinding = ListItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = itemBinding.root
        return CalendarItemHolder(itemBinding)
    }

    override fun getItemCount(): Int = dataList.size

    inner class CalendarItemHolder(itemView: ListItemCalendarBinding) : RecyclerView.ViewHolder(itemView.root) {

        //var itemCalendarDateText: TextView = itemView!!.item_calendar_date_text
        var itemCalendarDateText: TextView = itemView.itemCalendarDateText

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(data: Int, position: Int, context: Context) {
            val firstDateIndex = furangCalendar.prevTail
            val lastDateIndex = dataList.size - furangCalendar.nextHead - 1

            // 날짜 표시
            itemCalendarDateText.setText(data.toString())

            // 오늘 날짜 처리
            var dateString: String = SimpleDateFormat("dd", Locale.KOREA).format(date)
            var dateInt = dateString.toInt()
            if (dataList[position] == dateInt) {
                itemCalendarDateText.setTypeface(itemCalendarDateText.typeface, Typeface.BOLD)
            }

            // 현재 월의 1일 이전, 현재 월의 마지막일 이후 값의 텍스트를 회색처리
            if (position < firstDateIndex || position > lastDateIndex) {
                itemCalendarDateText.setTextAppearance(R.style.LightColorTextViewStyle)
                //itemCalendarDotView.background = null
            }
        }

    }
}