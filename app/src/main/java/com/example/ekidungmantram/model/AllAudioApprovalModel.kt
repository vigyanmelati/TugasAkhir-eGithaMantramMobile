package com.example.ekidungmantram.model

data class AllAudioApprovalModel(
    val id_audio: Int,
    val id_dharmagita: Int,
    val id_tag: Int,
    val judul_audio: String,
    val gambar_audio: String,
    val is_approved_audio: Int,
    val nama_tag: String,
    val nama_post: String,
    val is_approved_post: Int,
)
