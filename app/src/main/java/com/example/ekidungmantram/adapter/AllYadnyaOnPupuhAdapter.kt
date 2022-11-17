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
import com.example.ekidungmantram.adapter.admin.AllYadnyaOnPupuhAdminAdapter
import com.example.ekidungmantram.model.YadnyaPupuhModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel

class AllYadnyaOnPupuhAdapter (private var results: ArrayList<YadnyaPupuhModel.DataL>)
    : RecyclerView.Adapter<AllYadnyaOnPupuhAdapter.ViewHolder>() {
    private var onclickDelete: ((YadnyaPupuhModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_yadnya_on_pupuh_user, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.delete.setOnClickListener {
            onclickDelete?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var delete : ImageView = view.findViewById(R.id.deleteYadnyaOnPupuhUser)
        private var title : TextView = view.findViewById(R.id.titleOP_yadnya_user)
        private var jenis : TextView = view.findViewById(R.id.jenisOP_yadnya_user)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaOP_imgs_user)
        fun bindItem(data: YadnyaPupuhModel.DataL) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.nama_post
            jenis.text = "Yadnya Terkait"
            if(data.gambar != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.sample_image_tabuh)
            }
        }

    }

    fun setOnClickDelete(callback: ((YadnyaPupuhModel.DataL)->Unit)){
        this.onclickDelete = callback
    }
}