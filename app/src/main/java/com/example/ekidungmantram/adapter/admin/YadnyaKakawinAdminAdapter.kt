package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.YadnyaKakawinAdminModel

class YadnyaKakawinAdminAdapter (val results:ArrayList<YadnyaKakawinAdminModel.DataL>, val listener: OnAdapterYadnyaKakawinAdminListener)
    : RecyclerView.Adapter<YadnyaKakawinAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_yadnya_kakawin, parent, false)
    )

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val gambar: ImageView = view.findViewById(R.id.yadnya_kakawin_img)
        val title: TextView = view.findViewById(R.id.title_yadnya_kakawin)
        val kategori: TextView = view.findViewById(R.id.kategori_yadnya_kakawin)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.kategori.setText(result.kategori)
//        holder.gambar.setImageResource(result.gambar)
        Glide.with(holder.view).load(result.gambar).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount() = results.size

    fun setData (data: List<YadnyaKakawinAdminModel.DataL>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterYadnyaKakawinAdminListener {
        fun onClick(result: YadnyaKakawinAdminModel.DataL)
    }
}