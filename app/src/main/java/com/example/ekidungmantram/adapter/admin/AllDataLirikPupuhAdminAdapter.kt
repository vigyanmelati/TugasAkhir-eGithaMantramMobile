package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AllLirikKidungAdminModel
import com.example.ekidungmantram.model.adminmodel.AllLirikPupuhAdminModel

class AllDataLirikPupuhAdminAdapter (private var results: ArrayList<AllLirikPupuhAdminModel> )
    : RecyclerView.Adapter<AllDataLirikPupuhAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AllLirikPupuhAdminModel)->Unit)? = null
    private var onclickDelete: ((AllLirikPupuhAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_lirik_pupuh, parent, false)
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
        private var urutan : TextView = view.findViewById(R.id.urutan_list_lirik_pupuh)
        private var lirik : TextView = view.findViewById(R.id.lirik_pupuh)
        var editl : ImageView = view.findViewById(R.id.goToEditLirikPupuh)
        var delete : ImageView = view.findViewById(R.id.deleteLirikPupuh)
        fun bindItem(data: AllLirikPupuhAdminModel) {
            urutan.text = data.urutan.toString() + "."
            lirik.text = data.bait
        }

    }

    fun setOnClickEdit(callback: ((AllLirikPupuhAdminModel)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AllLirikPupuhAdminModel)->Unit)){
        this.onclickDelete = callback
    }
}