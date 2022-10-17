package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.DetailBaitKakawinModel
import com.example.ekidungmantram.model.DetailBaitPupuhModel

class BaitKakawinAdapter (val results: ArrayList<DetailBaitKakawinModel.Data>)
    : RecyclerView.Adapter<BaitKakawinAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_detail_kakawin, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bait.setText(result.urutan)
        holder.isibait.setText(result.bait)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val bait: TextView = view.findViewById(R.id.bait)
        val isibait: TextView = view.findViewById(R.id.isiBait)
    }

    fun setData (data: List<DetailBaitKakawinModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}