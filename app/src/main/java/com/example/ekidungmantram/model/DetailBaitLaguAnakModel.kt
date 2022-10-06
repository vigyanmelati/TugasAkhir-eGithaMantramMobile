package com.example.ekidungmantram.model

data class DetailBaitLaguAnakModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
    )
}
