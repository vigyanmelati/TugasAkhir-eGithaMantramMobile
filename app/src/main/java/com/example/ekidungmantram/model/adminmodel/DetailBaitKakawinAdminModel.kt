package com.example.ekidungmantram.model.adminmodel

data class DetailBaitKakawinAdminModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
    )
}