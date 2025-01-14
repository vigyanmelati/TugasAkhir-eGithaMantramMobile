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
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.database.data.Yadnya

class BookmarkedDharmagitaAdapter (val results: ArrayList<Dharmagita>, val listener: OnAdapterListener)
    : RecyclerView.Adapter<BookmarkedDharmagitaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_bookmarked, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
        holder.jenis.setText(result.nama_tag)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar).into(holder.gambar)
//        Glide.with(holder.view).load(result.gambar).into(holder.gambar)
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.title_bookmarked_yadnya)
        val jenis: TextView = view.findViewById(R.id.jenis__bookmarked_yadnya)
        val gambar: ImageView = view.findViewById(R.id.bookmarkedImage)
    }

    fun setData (data: List<Dharmagita>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(result: Dharmagita)
    }
}