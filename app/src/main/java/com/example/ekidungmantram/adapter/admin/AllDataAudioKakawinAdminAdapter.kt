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
import com.example.ekidungmantram.model.adminmodel.AudioKakawinAdminModel

class AllDataAudioKakawinAdminAdapter (private var results: ArrayList<AudioKakawinAdminModel.DataL> )
    : RecyclerView.Adapter<AllDataAudioKakawinAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AudioKakawinAdminModel.DataL)->Unit)? = null
    private var onclickDelete: ((AudioKakawinAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_audio_kakawin_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val audio : ImageView = view.findViewById(R.id.audio_kakawin_baru)
        val tittle : TextView = view.findViewById(R.id.title_audio_kakawin_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditAudioKakawin)
        var delete : ImageView = view.findViewById(R.id.deleteAudioKakawin)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tittle.setText(result.judul_audio)
//        Glide.with(holder.view).load(result.gambar_audio).into(holder.audio)
//        Glide.with(holder.view).load(Constant.IMAGE_URL + result.gambar_audio).into(holder.audio)
        holder.editl.setOnClickListener {
            onclickEdit?.invoke(result)
        }
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size


    fun setOnClickEdit(callback: ((AudioKakawinAdminModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AudioKakawinAdminModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}