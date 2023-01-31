package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.VideoKidungAdminModel

class VideoKidungAdminAdapter (val results:ArrayList<VideoKidungAdminModel.DataL>, val listener: OnAdapterVideoKidungAdminListener)
    : RecyclerView.Adapter<VideoKidungAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_video_kidung, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.video_kidung_img)
        val title: TextView = view.findViewById(R.id.title_video_kidung)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.judul_video)
//        holder.gambar.setImageResource(result.gambar_video)
//        Glide.with(holder.view).load(result.gambar_video).into(holder.gambar)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_video).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<VideoKidungAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterVideoKidungAdminListener {
        fun onClick(result: VideoKidungAdminModel.DataL)
    }
}