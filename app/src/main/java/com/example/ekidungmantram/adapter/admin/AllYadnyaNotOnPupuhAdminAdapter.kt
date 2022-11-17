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
import com.example.ekidungmantram.model.adminmodel.AllTabuhAdminModel
import com.example.ekidungmantram.model.adminmodel.YadnyaPupuhAdminModel

class AllYadnyaNotOnPupuhAdminAdapter (private var results: ArrayList<YadnyaPupuhAdminModel.DataL>)
    : RecyclerView.Adapter<AllYadnyaNotOnPupuhAdminAdapter.ViewHolder>() {
    private var onclickAdd: ((YadnyaPupuhAdminModel.DataL)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_yadnya_not_on_pupuh_admin, parent, false)
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
        var add : ImageView = view.findViewById(R.id.addYadnyaToPupuh)
        private var title : TextView = view.findViewById(R.id.titleNP_yadnya_admin)
        private var jenis : TextView = view.findViewById(R.id.jenisNP_yadnya_admin)
        private var gambar : ImageView = view.findViewById(R.id.yadnyaNP_imgs_admin)
        fun bindItem(data: YadnyaPupuhAdminModel.DataL) {
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

    fun setOnClickAdd(callback: ((YadnyaPupuhAdminModel.DataL)->Unit)){
        this.onclickAdd = callback
    }
}