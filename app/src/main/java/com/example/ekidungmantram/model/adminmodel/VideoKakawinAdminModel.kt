package com.example.ekidungmantram.model.adminmodel

data class VideoKakawinAdminModel(val data: ArrayList<DataL>) {
    data class DataL(
        val id_video: Int,
        val id_dharmagita: Int,
        val judul_video: String,
        val gambar_video: String,
        val video: String
    )
}
