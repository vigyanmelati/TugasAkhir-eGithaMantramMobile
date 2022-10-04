package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.NewMantramModel
import com.example.ekidungmantram.model.NewPupuhModel

class NewPupuhAdapter (val results: ArrayList<NewPupuhModel.DataP>, val listener: OnAdapterPupuhListener)
    : RecyclerView.Adapter<NewPupuhAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_mantram, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.title.setText(result.nama_post)
//        holder.jenis.setText("Pupuh "+result.kategori)
        if(result.nama_post.length > 20){
            holder.title.textSize = 15F
        }
        holder.view.setOnClickListener{
            listener.onClick(result)
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_mantram_baru)
    }

    override fun getItemCount() = results.size

    fun setData (data: List<NewPupuhModel.DataP>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterPupuhListener {
        fun onClick(result: NewPupuhModel.DataP)
    }
}