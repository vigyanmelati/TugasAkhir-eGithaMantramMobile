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
import com.example.ekidungmantram.model.AllDharmagitaUserModel
import com.example.ekidungmantram.model.KategoriPupuhModel

class AllDharmagitaUserAdapter (private var results: ArrayList<AllDharmagitaUserModel>, val listener: OnAdapterAllDharmagitaUserListener)
    : RecyclerView.Adapter<AllDharmagitaUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_dhar, parent, false)
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
        private var title : TextView = view.findViewById(R.id.title_dhar)
        private var gambar : ImageView = view.findViewById(R.id.dhar_imgs)
        fun bindItem(data: AllDharmagitaUserModel) {
            if(data.nama_post.length > 30){
                title.textSize = 15F
            }
            title.text = data.nama_post
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
//                Glide.with(itemView).load(data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }
    }

    interface OnAdapterAllDharmagitaUserListener {
        fun onClick(result: AllDharmagitaUserModel)
    }
}