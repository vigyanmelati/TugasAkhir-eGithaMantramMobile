package com.example.ekidungmantram.model.adminmodel

data class AudioKakawinAdminModel(val data: ArrayList<DataL>) {
    data class DataL(
        val id_audio: Int,
        val id_dharmagita: Int,
        val judul_audio: String,
        val gambar_audio: String,
        val audio: String
    )
}
