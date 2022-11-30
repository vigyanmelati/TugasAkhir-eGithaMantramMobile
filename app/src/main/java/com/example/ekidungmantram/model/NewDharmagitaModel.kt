package com.example.ekidungmantram.model

data class NewDharmagitaModel(val data: ArrayList<Data>){
    data class Data(
        val id_post: Int,
        val id_tag: Int,
        val deskripsi: String,
        val nama_post: String,
        val nama_tag: String,
        val gambar: String
    )
}
