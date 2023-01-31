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
import com.example.ekidungmantram.model.AudioLaguAnakModel
import com.example.ekidungmantram.model.VideoLaguAnakModel

data class AudioLaguAnakAdapter(val results:ArrayList<AudioLaguAnakModel.DataL>, val listener: OnAdapterAudioLaguAnakListener)
    : RecyclerView.Adapter<AudioLaguAnakAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_audio_lagu_anak, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.audio_lagu_anak_img)
        val title: TextView = view.findViewById(R.id.title_audio_lagu_anak)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.judul_audio)
//        holder.gambar.setImageResource(result.gambar_audio)
//        Glide.with(holder.view).load(result.gambar_audio).into(holder.gambar)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_audio).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<AudioLaguAnakModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterAudioLaguAnakListener {
        fun onClick(result: AudioLaguAnakModel.DataL)
    }
}
