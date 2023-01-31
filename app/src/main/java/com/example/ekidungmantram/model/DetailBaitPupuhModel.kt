package com.example.ekidungmantram.model

data class DetailBaitPupuhModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val bait: String,
        val arti:String
    )
}
