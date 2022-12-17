package com.example.ekidungmantram.model.adminmodel

data class YadnyaKidungAdminModel(val data: ArrayList<DataL>) {
    data class DataL(
        val id: Int,
        val id_post: Int,
        val id_kategori: Int,
        val kategori: String,
        val nama_post: String,
        val gambar: String
    )
}
