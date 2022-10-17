package com.example.ekidungmantram.model

data class YadnyaLaguAnakModel(val data: ArrayList<DataL>) {
    data class DataL(
        val id_post: Int,
        val id_kategori: Int,
        val kategori: String,
        val nama_post: String,
        val gambar: String
    )
}
