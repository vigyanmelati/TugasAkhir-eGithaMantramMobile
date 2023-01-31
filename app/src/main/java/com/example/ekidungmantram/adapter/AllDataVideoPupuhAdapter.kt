package com.example.ekidungmantram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.adapter.admin.AllDataVideoPupuhAdminAdapter
import com.example.ekidungmantram.model.VideoPupuhModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel

class AllDataVideoPupuhAdapter (private var results: ArrayList<VideoPupuhModel.DataL> )
    : RecyclerView.Adapter<AllDataVideoPupuhAdapter.ViewHolder>() {
    private var onclickEdit: ((VideoPupuhModel.DataL)->Unit)? = null
    private var onclickDelete: ((VideoPupuhModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_video_pupuh_user, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val video : ImageView = view.findViewById(R.id.video_pupuh_baru)
        val tittle : TextView = view.findViewById(R.id.title_video_pupuh_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditVideoPupuhUser)
        var delete : ImageView = view.findViewById(R.id.deleteVideoPupuhUser)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tittle.setText(result.judul_video)
//        Glide.with(holder.view).load(result.gambar_video).into(holder.video)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_video).into(holder.video)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size


    fun setOnClickEdit(callback: ((VideoPupuhModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((VideoPupuhModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}