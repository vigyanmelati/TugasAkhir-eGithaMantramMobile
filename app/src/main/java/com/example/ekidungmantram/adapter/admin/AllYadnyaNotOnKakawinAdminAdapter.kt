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
import com.example.ekidungmantram.model.adminmodel.YadnyaKakawinAdminModel

class AllYadnyaNotOnKakawinAdminAdapter (private var results: ArrayList<YadnyaKakawinAdminModel.DataL>)
    : RecyclerView.Adapter<AllYadnyaNotOnKakawinAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((YadnyaKakawinAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_yadnya_not_on_kakawin_admin, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.add.setOnClickListener {
            onclickAdd?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var add : ImageView = view.findViewById(R.id.addYadnyaToKakawin)
        private var title : TextView = view.findViewById(R.id.titleNKW_yadnya_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNKW_yadnya_admin)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaNKW_imgs_admin)
        fun bindItem(data: YadnyaKakawinAdminModel.DataL) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
//            jenis.text = "Tabuh Bali"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }

    }

    fun setOnClickAdd(callback: ((YadnyaKakawinAdminModel.DataL)->Unit)){
        this.onclickAdd = callback
    }
}