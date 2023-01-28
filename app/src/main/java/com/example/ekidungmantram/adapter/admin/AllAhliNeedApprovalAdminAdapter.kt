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
import com.example.ekidungmantram.model.AdminModel
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel
import com.example.ekidungmantram.model.adminmodel.AllMantramAdminModel

class AllAhliNeedApprovalAdminAdapter (private var results: ArrayList<AllDataAdminModel>, val listener: OnAdapterAllAhliNAAdminListener)
    : RecyclerView.Adapter<AllAhliNeedApprovalAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_ahli_need_approval, parent, false)
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
        private var title : TextView = view.findViewById(R.id.titleNA_ahli_admin)
        private var jenis : TextView = view.findViewById(R.id.emailNA_ahli_admin)
        private var gambar : ImageView = view.findViewById(R.id.ahliNA_img_admin)
        fun bindItem(data: AllDataAdminModel) {
            if(data.name.length > 20){
                title.textSize = 15F
            }
            title.text = data.name
            jenis.text = data.email
        }

    }

    interface OnAdapterAllAhliNAAdminListener {
        fun onClick(result: AllDataAdminModel)
    }
}