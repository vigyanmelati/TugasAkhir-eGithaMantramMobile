package com.example.ekidungmantram.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekidungmantram.R
import com.example.ekidungmantram.model.adminmodel.AllDataAdminModel

class AllDataUserAdapter (private var results: ArrayList<AllDataAdminModel>, val listener: OnAdapterAllDataUserListener)
    : RecyclerView.Adapter<AllDataUserAdapter.ViewHolder>() {
    private var onclickName: ((AllDataAdminModel)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView = layoutInflater.inflate(R.layout.layout_all_user, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bindItem(result)
        holder.itemView.setOnClickListener{
            listener.onClick(result)
        }
        holder.name.setOnClickListener {
            onclickName?.invoke(result)
        }
    }

    override fun getItemCount(): Int = results.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name : TextView = view.findViewById(R.id.user_name)
        private var email : TextView = view.findViewById(R.id.user_email)
        fun bindItem(data: AllDataAdminModel) {
            if(data.name.length > 20){
                name.textSize = 15F
            }
            name.text = data.name
            email.text = data.email
        }

    }

    fun setOnClickName(callback: ((AllDataAdminModel)->Unit)){
        this.onclickName = callback
    }

    interface OnAdapterAllDataUserListener {
        fun onClick(result: AllDataAdminModel)
    }
}