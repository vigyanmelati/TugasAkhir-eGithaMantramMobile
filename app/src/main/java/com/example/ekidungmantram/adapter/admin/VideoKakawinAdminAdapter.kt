package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.VideoKakawinAdminModel

class VideoKakawinAdminAdapter (val results:ArrayList<VideoKakawinAdminModel.DataL>, val listener: OnAdapterVideoKakawinAdminListener)
    : RecyclerView.Adapter<VideoKakawinAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_video_kakawin, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.video_kakawin_img)
        val title: TextView = view.findViewById(R.id.title_video_kakawin)
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

    fun setData (data: List<VideoKakawinAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterVideoKakawinAdminListener {
        fun onClick(result: VideoKakawinAdminModel.DataL)
    }
}