package com.example.ekidungmantram.model

data class DetailBaitKakawinModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
        val arti:String
    )
}
