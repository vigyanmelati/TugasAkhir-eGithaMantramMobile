package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.DetailBaitKakawinModel

class ArtiKakawinAdapter (val results: ArrayList<DetailBaitKakawinModel.Data>)
    : RecyclerView.Adapter<ArtiKakawinAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_arti_kakawin, parent, false)
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

    fun setData (data: List<DetailBaitKakawinModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}