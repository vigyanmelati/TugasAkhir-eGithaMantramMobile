package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.VideoKidungAdminModel

class AllDataVideoKidungAdminAdapter (private var results: ArrayList<VideoKidungAdminModel.DataL> )
    : RecyclerView.Adapter<AllDataVideoKidungAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((VideoKidungAdminModel.DataL)->Unit)? = null
    private var onclickDelete: ((VideoKidungAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_video_kidung_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val video : ImageView = view.findViewById(R.id.video_kidung_baru)
        val tittle : TextView = view.findViewById(R.id.title_video_kidung_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditVideoKidung)
        var delete : ImageView = view.findViewById(R.id.deleteVideoKidung)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tittle.setText(result.judul_video)
        Glide.with(holder.view).load(result.gambar_video).into(holder.video)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size


    fun setOnClickEdit(callback: ((VideoKidungAdminModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((VideoKidungAdminModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}