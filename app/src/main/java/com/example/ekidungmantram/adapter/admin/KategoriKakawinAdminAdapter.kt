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
import com.example.ekidungmantram.model.adminmodel.KategoriKakawinAdminModel
import com.example.ekidungmantram.model.adminmodel.KategoriPupuhAdminModel

class KategoriKakawinAdminAdapter (private var results: ArrayList<KategoriKakawinAdminModel>, val listener: OnAdapterKategoriKakawinAdminListener)
    : RecyclerView.Adapter<KategoriKakawinAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_kategori_kakawin_admin, parent, false)
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
        private var title : TextView = view.findViewById(R.id.title_kategori_kakawinAdmin)
        private var gambar : ImageView = view.findViewById(R.id.kategori_kakawin_imgsAdmin)
        fun bindItem(data: KategoriKakawinAdminModel) {
            if(data.nama_post.length > 30){
                title.textSize = 15F
            }
            title.text = data.nama_post
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }
    }

    interface OnAdapterKategoriKakawinAdminListener {
        fun onClick(result: KategoriKakawinAdminModel)
    }
}