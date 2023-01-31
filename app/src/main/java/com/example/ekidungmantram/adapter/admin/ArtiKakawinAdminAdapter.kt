package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.ArtiKakawinAdapter
import com.example.ekidungmantram.model.DetailBaitKakawinModel
import com.example.ekidungmantram.model.adminmodel.DetailBaitKakawinAdminModel

class ArtiKakawinAdminAdapter (val results: ArrayList<DetailBaitKakawinAdminModel.Data>)
    : RecyclerView.Adapter<ArtiKakawinAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_arti_kakawin_admin, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.arti.setText(result.urutan)
        holder.isiArti.setText(result.arti)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val arti: TextView = view.findViewById(R.id.arti_admin)
        val isiArti: TextView = view.findViewById(R.id.isiArti_admin)
    }

    fun setData (data: List<DetailBaitKakawinAdminModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}