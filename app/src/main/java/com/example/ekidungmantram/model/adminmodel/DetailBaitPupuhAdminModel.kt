package com.example.ekidungmantram.model.adminmodel

data class DetailBaitPupuhAdminModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
    )
}
