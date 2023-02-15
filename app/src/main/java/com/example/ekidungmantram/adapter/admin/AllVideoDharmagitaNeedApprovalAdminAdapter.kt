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
import com.example.ekidungmantram.model.AllDharmagitaApprovalModel
import com.example.ekidungmantram.model.AllVideoApprovalModel

class AllVideoDharmagitaNeedApprovalAdminAdapter (private var results: ArrayList<AllVideoApprovalModel>, val listener: AllVideoDharmagitaNeedApprovalAdminAdapter.OnAdapterAllVideoDharmagitaNAAdminListener)
    : RecyclerView.Adapter<AllVideoDharmagitaNeedApprovalAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_list_video_na_admin, parent, false)
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
        private var title : TextView = view.findViewById(R.id.title_video_NAAdmin)
//        private var jenis : TextView = view.findViewById(R.id.jenis_video_NAAdmin)
        private var nama_post : TextView = view.findViewById(R.id.nama_post_video_NAAdmin)
        private var gambar : ImageView = view.findViewById(R.id.video_NAAdmin)
        fun bindItem(data: AllVideoApprovalModel) {
            if(data.nama_post.length > 20){
                title.textSize = 15F
            }
            title.text = data.judul_video
//            if(data.nama_tag != null){
//                jenis.text = data.nama_tag
//            }else{
//                jenis.text = "Dharmagita"
//            }
            nama_post.text = data.nama_post
            if(data.gambar_video != null){
                Glide.with(itemView).load(Constant.IMAGE_URL + data.gambar_video).into(gambar)
//                Glide.with(itemView).load(data.gambar_video).into(gambar)
            }else{
                gambar.setImageResource(R.drawable.meditation)
            }
        }

    }

    interface OnAdapterAllVideoDharmagitaNAAdminListener {
        fun onClick(result: AllVideoApprovalModel)
    }
}