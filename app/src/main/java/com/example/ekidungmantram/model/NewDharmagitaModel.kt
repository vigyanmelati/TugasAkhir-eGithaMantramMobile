package com.example.ekidungmantram.model

data class NewDharmagitaModel(val data: ArrayList<Data>){
    data class Data(
        val id_post: Int,
        val deskripsi: String,
        val nama_post: String,
        val gambar: String
    )
}
