package com.letscamping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.letscamping.databinding.BannerListItemBinding
import com.letscamping.databinding.FragmentCarouselBinding
import kotlinx.parcelize.Parcelize
/*import kotlinx.android.synthetic.main.banner_list_item.view.*
import kotlinx.android.synthetic.main.fragment_carousel.**/

class CarouselFragment : Fragment() {
    private lateinit var binding:FragmentCarouselBinding

    private var numBanner = 3
    private var currentPosition = Int.MAX_VALUE / 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarouselBinding.inflate(layoutInflater)
        //val view = inflater.inflate(R.layout.fragment_carousel,container , false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerBanner.adapter = ViewPagerAdapter(getBannerList())
        binding.viewPagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerBanner.setCurrentItem(currentPosition, false) // 현재 위치를 지정
        binding.textViewTotalBanner.text = numBanner.toString()

        // 현재 몇번째 배너인지 보여주는 숫자를 변경함
        binding.viewPagerBanner.apply {
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.textViewCurrentBanner.text = "${(position%numBanner)+1}" // 여기서도 %3
                }
            })
        }
        binding.linearLayoutSeeAll.setOnClickListener {
            Toast.makeText(requireContext(), "모두 보기 클릭했음", Toast.LENGTH_LONG).show()
        }
    }
    private fun getBannerList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3)
    }

    /*class ViewPagerAdapter(
        bannerList: ArrayList<Int>
    ) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

        var item = bannerList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

        override fun getItemCount(): Int = Int.MAX_VALUE	// 아이템의 갯수를 임의로 확 늘린다.

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            holder.banner.setScaleType(ImageView.ScaleType.CENTER)
            holder.banner.setImageResource(item[position%3])	// position에 3을 나눈 나머지 값을 사용한다.
        }

        inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
            (LayoutInflater.from(parent.context).inflate(R.layout.banner_list_item, parent, false)){
            val banner = itemView.imageView_banner!!
        }

    }*/

    class ViewPagerAdapter(
        bannerList: ArrayList<Int>
        ) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

        var item = bannerList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            val itemBinding = BannerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PagerViewHolder(itemBinding)
        }

        override fun getItemCount(): Int = Int.MAX_VALUE	// 아이템의 갯수를 임의로 확 늘린다.

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            holder.banner.setScaleType(ImageView.ScaleType.CENTER)
            holder.banner.setImageResource(item[position%3])	// position에 3을 나눈 나머지 값을 사용한다.
        }

        class PagerViewHolder(private val itemBinding: BannerListItemBinding) : RecyclerView.ViewHolder
            (itemBinding.root) {
                val banner = itemBinding.imageViewBanner
        }
    }
}
