package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllLaguAnakModel
import com.example.ekidungmantram.model.NewKidungModel
import com.example.ekidungmantram.model.VideoLaguAnakModel

class VideoLaguAnakAdapter (val results:ArrayList<VideoLaguAnakModel.DataL>, val listener: OnAdapterVideoLaguAnakListener)
    : RecyclerView.Adapter<VideoLaguAnakAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_video_lagu_anak, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.video_lagu_anak_img)
        val title: TextView = view.findViewById(R.id.title_video_lagu_anak)
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

    fun setData (data: List<VideoLaguAnakModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterVideoLaguAnakListener {
        fun onClick(result: VideoLaguAnakModel.DataL)
    }
}