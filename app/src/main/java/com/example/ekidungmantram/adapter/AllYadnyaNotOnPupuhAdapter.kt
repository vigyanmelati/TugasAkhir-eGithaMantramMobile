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
import com.example.ekidungmantram.adapter.admin.AllYadnyaNotOnPupuhAdminAdapter
import com.example.ekidungmantram.model.YadnyaPupuhModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel

class AllYadnyaNotOnPupuhAdapter (private var results: ArrayList<YadnyaPupuhModel.DataL>)
    : RecyclerView.Adapter<AllYadnyaNotOnPupuhAdapter.ViewHolder>() {
    private var onclickAdd: ((YadnyaPupuhModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_yadnya_not_on_pupuh_user, parent, false)
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
        var add : ImageView = view.findViewById(R.id.addYadnyaToPupuhUser)
        private var title : TextView = view.findViewById(R.id.titleNP_yadnya_user)
        private var jenis : TextView = view.findViewById(R.id.jenisNP_yadnya_user)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaNP_imgs_user)
        fun bindItem(data: YadnyaPupuhModel.DataL) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
//            jenis.text = "Tabuh Bali"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
//                Glide.with(itemView).load(data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_yadnya)
            }
        }

    }

    fun setOnClickAdd(callback: ((YadnyaPupuhModel.DataL)->Unit)){
        this.onclickAdd = callback
    }
}