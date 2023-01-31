package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.DetailBaitKakawinModel
import com.example.ekidungmantram.model.DetailBaitPupuhModel

class ArtiPupuhAdapter (val results: ArrayList<DetailBaitPupuhModel.Data>)
    : RecyclerView.Adapter<ArtiPupuhAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_arti_pupuh, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.arti.setText(result.urutan)
        holder.isiArti.setText(result.arti)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val arti: TextView = view.findViewById(R.id.arti)
        val isiArti: TextView = view.findViewById(R.id.isiArti)
    }

    fun setData (data: List<DetailBaitPupuhModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}