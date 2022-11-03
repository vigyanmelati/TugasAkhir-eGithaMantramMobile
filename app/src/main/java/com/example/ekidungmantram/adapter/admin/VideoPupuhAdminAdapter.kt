package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.VideoPupuhAdapter
import com.example.ekidungmantram.model.VideoPupuhModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel

class VideoPupuhAdminAdapter (val results:ArrayList<VideoPupuhAdminModel.DataL>, val listener: OnAdapterVideoPupuhAdminListener)
    : RecyclerView.Adapter<VideoPupuhAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_video_pupuh, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.video_pupuh_img)
        val title: TextView = view.findViewById(R.id.title_video_pupuh)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.judul_video)
//        holder.gambar.setImageResource(result.gambar_video)
        Glide.with(holder.view).load(result.gambar_video).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<VideoPupuhAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterVideoPupuhAdminListener {
        fun onClick(result: VideoPupuhAdminModel.DataL)
    }
}