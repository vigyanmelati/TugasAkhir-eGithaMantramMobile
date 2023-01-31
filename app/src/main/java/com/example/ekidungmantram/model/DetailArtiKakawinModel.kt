package com.example.ekidungmantram.model

data class DetailArtiKakawinModel(val data: ArrayList<Data>){
    data class Data(
        val urutan: String,
        val arti:String
    )
}
