package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.DetailBaitKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.DetailBaitPupuhAdminModel

class ArtiPupuhAdapter (val results: ArrayList<DetailBaitPupuhAdminModel.Data>)
    : RecyclerView.Adapter<ArtiPupuhAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_arti_pupuh_admin, parent, false)
    )

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.arti.setText(result.urutan)
        holder.isiArti.setText(result.arti)
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view){
        val arti: TextView = view.findViewById(R.id.arti_admin_pupuh)
        val isiArti: TextView = view.findViewById(R.id.isiArti_admin_pupuh)
    }

    fun setData (data: List<DetailBaitPupuhAdminModel.Data>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }
}