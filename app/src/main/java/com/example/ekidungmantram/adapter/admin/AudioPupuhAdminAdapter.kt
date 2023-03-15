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
import com.example.ekidungmantram.adapter.AudioPupuhAdapter
import com.example.ekidungmantram.model.AudioPupuhModel
import com.example.ekidungmantram.model.adminmodel.AudioPupuhAdminModel

class AudioPupuhAdminAdapter (val results:ArrayList<AudioPupuhAdminModel.DataL>, val listener: OnAdapterAudioPupuhAdminListener)
    : RecyclerView.Adapter<AudioPupuhAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_audio_pupuh, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.audio_pupuh_img)
        val title: TextView = view.findViewById(R.id.title_audio_pupuh)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.judul_audio)
//        holder.gambar.setImageResource(result.gambar_audio)
//        Glide.with(holder.view).load(result.gambar_audio).into(holder.gambar)
//        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_audio).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<AudioPupuhAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterAudioPupuhAdminListener {
        fun onClick(result: AudioPupuhAdminModel.DataL)
    }
}