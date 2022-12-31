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
import com.example.ekidungmantram.model.VideoLaguAnakModel
import com.example.ekidungmantram.model.YadnyaLaguAnakModel

class YadnyaLaguAnakAdapter (val results:ArrayList<YadnyaLaguAnakModel.DataL>, val listener: OnAdapterYadnyaLaguAnakListener)
    : RecyclerView.Adapter<YadnyaLaguAnakAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_yadnya_lagu_anak, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.yadnya_lagu_anak_img)
        val title: TextView = view.findViewById(R.id.title_yadnya_lagu_anak)
        val kategori: TextView = view.findViewById(R.id.kategori_yadnya_lagu_anak)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.kategori.setText(result.kategori)
//        holder.gambar.setImageResource(result.gambar)
//        Glide.with(holder.view).load(result.gambar).into(holder.gambar)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<YadnyaLaguAnakModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterYadnyaLaguAnakListener {
        fun onClick(result: YadnyaLaguAnakModel.DataL)
    }
}