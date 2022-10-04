package com.example.ekidungmantram.model

data class NewPupuhModel(val data: ArrayList<DataP>) {
    data class DataP(
        val id_post: Int,
        val id_kategori: Int,
        val id_tag: Int,
        val kategori: String,
        val nama_post: String
    )
}