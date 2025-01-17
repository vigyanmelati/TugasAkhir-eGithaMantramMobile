package com.example.ekidungmantram.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.VideoLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.VideoPupuhAdminModel

class AllDataVideoLaguAnakAdminAdapter (private var results: ArrayList<VideoLaguAnakAdminModel.DataL> )
    : RecyclerView.Adapter<AllDataVideoLaguAnakAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((VideoLaguAnakAdminModel.DataL)->Unit)? = null
    private var onclickDelete: ((VideoLaguAnakAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_video_lagu_anak_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val video : ImageView = view.findViewById(R.id.video_lagu_anak_baru)
        val tittle : TextView = view.findViewById(R.id.title_video_lagu_anak_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditVideoLaguAnak)
        var delete : ImageView = view.findViewById(R.id.deleteVideoLaguAnak)

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


    fun setOnClickEdit(callback: ((VideoLaguAnakAdminModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((VideoLaguAnakAdminModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}