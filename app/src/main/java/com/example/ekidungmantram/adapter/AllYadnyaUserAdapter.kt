package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllYadnyaAdminAdapter
import com.example.ekidungmantram.model.adminmodel.AllYadnyaHomeAdminModel

class AllYadnyaUserAdapter (private var results: ArrayList<AllYadnyaHomeAdminModel>, val listener: OnAdapterAllYadnyaHomeAdminListener)
    : RecyclerView.Adapter<AllYadnyaUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView   = layoutInflater.inflate(R.layout.layout_list_all_yadnya_user, parent, false)
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
        private var title  : TextView = view.findViewById(R.id.nama_yadnyas_user)

        fun bindItem(data: AllYadnyaHomeAdminModel) {
            title.text = data.nama_kategori
        }

    }

    interface OnAdapterAllYadnyaHomeAdminListener {
        fun onClick(result: AllYadnyaHomeAdminModel)
    }
}