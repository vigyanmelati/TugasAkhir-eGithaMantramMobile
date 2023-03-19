package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllYadnyaHomeAdminAdapter
import com.example.ekidungmantram.model.AllDharmagitaHomePenggunaModel
import com.example.ekidungmantram.model.adminmodel.AllDharmagitaHomeAdminModel

class AllDharmagitaHomeAdapter (private var results: ArrayList<AllDharmagitaHomePenggunaModel>, val listener: OnAdapterAllDharmagitaHomePenggunaListener)
    : RecyclerView.Adapter<AllDharmagitaHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView   = layoutInflater.inflate(R.layout.layout_list_all_dharmagita_home, parent, false)
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
        private var title  : TextView = view.findViewById(R.id.nama_dharmagita_pengguna)
        private var jumlah : TextView = view.findViewById(R.id.jumlah_dharmagita_pengguna)

        fun bindItem(data: AllDharmagitaHomePenggunaModel) {
            title.text = data.nama_tag
            jumlah.text = data.jumlah.toString()
        }

    }

    interface OnAdapterAllDharmagitaHomePenggunaListener {
        fun onClick(result: AllDharmagitaHomePenggunaModel)
    }
}