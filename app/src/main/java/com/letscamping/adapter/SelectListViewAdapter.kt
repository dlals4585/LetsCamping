package com.letscamping.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.letscamping.DetailCampsiteActivity
import com.letscamping.databinding.ActivityCampinglist1Binding
import com.letscamping.model.CampingList
import com.squareup.picasso.Picasso
import java.io.IOException
import java.lang.Exception

//--------------------------------------------------------------------------------

class SelectListViewAdapter(
    var context: Context,
    var mCamplist: ArrayList<CampingList> = ArrayList<CampingList>()
) : RecyclerView.Adapter<SelectListViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectListViewAdapter.ViewHolder {
        val itemBinding = ActivityCampinglist1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return mCamplist.size
    }

    override fun onBindViewHolder(holder: SelectListViewAdapter.ViewHolder, position: Int) {
        try {
            val uri = Uri.parse(mCamplist.get(position).jsonObject.get("firstImageUrl").asString.toString())
            Picasso.with(this.context).load(uri).into(holder.mainImg)
        } catch (e1: IOException) {
            println("error=$e1")
        }catch (e: Exception){
            println("error1=$e")
        }

        holder.maintitle.text = mCamplist.get(position).jsonObject.get("facltNm").asString.toString()
        holder.contenttext.text = mCamplist.get(position).jsonObject.get("induty").asString.toString()
    }

    inner class ViewHolder(itemView: ActivityCampinglist1Binding) : RecyclerView.ViewHolder(itemView.root){
        val mainImg : ImageView = itemView.mainimg
        val maintitle : TextView = itemView.maintitle
        val contenttext : TextView = itemView.contenttext

        init{
            mainImg.scaleType = ImageView.ScaleType.FIT_XY

            itemView.root.setOnClickListener {
                val position: Int = adapterPosition
                val facltNm = mCamplist.get(position).jsonObject.get("facltNm").asString.toString()
                val contentId = mCamplist.get(position).jsonObject.get("contentId").asString.toString()

                val intent = Intent(it.context, DetailCampsiteActivity::class.java).apply {
                    putExtra("facltNm",facltNm)
                    putExtra("contentId",contentId)
                }
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}
//--------------------------------------------------------------------------------