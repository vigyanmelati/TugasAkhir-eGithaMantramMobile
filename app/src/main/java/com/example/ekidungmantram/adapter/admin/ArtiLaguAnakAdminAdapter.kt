package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.DetailBaitKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailBaitLaguAnakAdminModel

class ArtiLaguAnakAdminAdapter (val results: ArrayList<DetailBaitLaguAnakAdminModel.Data>)
    : RecyclerView.Adapter<ArtiLaguAnakAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_arti_lagu_anak_admin, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.arti.setText(result.urutan)
        holder.isiArti.setText(result.arti)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val arti: TextView = view.findViewById(R.id.arti_admin_lagu_anak)
        val isiArti: TextView = view.findViewById(R.id.isiArti_admin_lagu_anak)
    }

    fun setData (data: List<DetailBaitLaguAnakAdminModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}