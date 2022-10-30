package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaHomeAdminModel
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel

class AllDharmagitaAdminAdapter (private var results: ArrayList<AllDharmagitaHomeAdminModel>, val listener: OnAdapterAllDharmagitaHomeAdminListener)
    : RecyclerView.Adapter<AllDharmagitaAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView   = layoutInflater.inflate(R.layout.layout_list_all_yadnya_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount(): Int = results.size


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var title  : TextView = view.findViewById(R.id.nama_yadnyas_admin)

        fun bindItem(data: AllDharmagitaHomeAdminModel) {
            title.text = data.nama_post
        }

    }

    interface OnAdapterAllDharmagitaHomeAdminListener {
        fun onClick(result: AllDharmagitaHomeAdminModel)
    }
}