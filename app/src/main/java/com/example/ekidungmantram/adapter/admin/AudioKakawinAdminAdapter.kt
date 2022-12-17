package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AudioKakawinAdminModel

class AudioKakawinAdminAdapter (val results:ArrayList<AudioKakawinAdminModel.DataL>, val listener: OnAdapterAudioKakawinAdminListener)
    : RecyclerView.Adapter<AudioKakawinAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_audio_kakawin, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.audio_kakawin_img)
        val title: TextView = view.findViewById(R.id.title_audio_kakawin)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.judul_audio)
//        holder.gambar.setImageResource(result.gambar_audio)
        Glide.with(holder.view).load(result.gambar_audio).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<AudioKakawinAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterAudioKakawinAdminListener {
        fun onClick(result: AudioKakawinAdminModel.DataL)
    }
}