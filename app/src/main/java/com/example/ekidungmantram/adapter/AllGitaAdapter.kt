package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.AllGamelanModel
import com.example.ekidungmantram.model.AllGitaModel
import kotlinx.android.synthetic.main.layout_all_gita.view.*

class AllGitaAdapter(private val listGita: ArrayList<AllGitaModel>, val listener: OnAdapterAllGitaListener) : RecyclerView.Adapter<AllGitaAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_all_gita, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val result = listGita[position]
        val (name, description) = listGita[position]
//        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvDescription.text = description
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
    }

    override fun getItemCount():Int = listGita.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.title_gita)
        var tvDescription: TextView = itemView.findViewById(R.id.des_gita)
    }

    interface OnAdapterAllGitaListener {
        fun onClick(result: AllGitaModel)
    }
}