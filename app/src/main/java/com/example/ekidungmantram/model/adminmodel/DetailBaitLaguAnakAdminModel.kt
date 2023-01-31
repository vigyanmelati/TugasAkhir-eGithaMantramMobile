package com.example.ekidungmantram.model.adminmodel

data class DetailBaitLaguAnakAdminModel (val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
        val arti: String,
    )
}