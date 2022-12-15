package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AllLirikKakawinAdminModel

class AllDataLirikKakawinAdminAdapter (private var results: ArrayList<AllLirikKakawinAdminModel> )
    : RecyclerView.Adapter<AllDataLirikKakawinAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AllLirikKakawinAdminModel)->Unit)? = null
    private var onclickDelete: ((AllLirikKakawinAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_lirik_kakawin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var urutan : TextView = view.findViewById(R.id.urutan_list_lirik_kakawin)
        private var lirik : TextView = view.findViewById(R.id.lirik_kakawin)
        var editl : ImageView = view.findViewById(R.id.goToEditLirikKakawin)
        var delete : ImageView = view.findViewById(R.id.deleteLirikKakawin)
        fun bindItem(data: AllLirikKakawinAdminModel) {
            urutan.text = data.urutan.toString() + "."
            lirik.text = data.bait
        }

    }

    fun setOnClickEdit(callback: ((AllLirikKakawinAdminModel)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AllLirikKakawinAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}