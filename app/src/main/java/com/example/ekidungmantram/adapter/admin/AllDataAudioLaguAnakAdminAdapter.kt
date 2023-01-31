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
import com.example.ekidungmantram.model.adminmodel.AudioLaguAnakAdminModel
import com.example.ekidungmantram.model.adminmodel.AudioPupuhAdminModel

class AllDataAudioLaguAnakAdminAdapter (private var results: ArrayList<AudioLaguAnakAdminModel.DataL> )
    : RecyclerView.Adapter<AllDataAudioLaguAnakAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AudioLaguAnakAdminModel.DataL)->Unit)? = null
    private var onclickDelete: ((AudioLaguAnakAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_audio_lagu_anak_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val audio : ImageView = view.findViewById(R.id.audio_lagu_anak_baru)
        val tittle : TextView = view.findViewById(R.id.title_audio_lagu_anak_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditAudioLaguAnak)
        var delete : ImageView = view.findViewById(R.id.deleteAudioLaguAnak)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tittle.setText(result.judul_audio)
//        Glide.with(holder.view).load(result.gambar_audio).into(holder.audio)
        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_audio).into(holder.audio)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size


    fun setOnClickEdit(callback: ((AudioLaguAnakAdminModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AudioLaguAnakAdminModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}