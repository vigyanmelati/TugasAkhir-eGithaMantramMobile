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
import com.example.ekidungmantram.model.adminmodel.AudioKidungAdminModel

class AllDataAudioKidungAdminAdapter (private var results: ArrayList<AudioKidungAdminModel.DataL> )
    : RecyclerView.Adapter<AllDataAudioKidungAdminAdapter.ViewHolder>() {
    private var onclickEdit: ((AudioKidungAdminModel.DataL)->Unit)? = null
    private var onclickDelete: ((AudioKidungAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_audio_kidung_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val audio : ImageView = view.findViewById(R.id.audio_kidung_baru)
        val tittle : TextView = view.findViewById(R.id.title_audio_kidung_baru)
        var editl : ImageView = view.findViewById(R.id.goToEditAudioKidung)
        var delete : ImageView = view.findViewById(R.id.deleteAudioKidung)

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


    fun setOnClickEdit(callback: ((AudioKidungAdminModel.DataL)->Unit)){
        this.onclickEdit = callback
    }

    fun setOnClickDelete(callback: ((AudioKidungAdminModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}