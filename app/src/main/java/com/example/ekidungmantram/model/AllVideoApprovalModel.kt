package com.example.ekidungmantram.model

data class AllVideoApprovalModel(
    val id_video: Int,
    val id_dharmagita: Int,
    val id_tag: Int,
    val judul_video: String,
    val gambar_video: String,
    val is_approved_video: Int,
    val nama_tag: String,
    val nama_post: String,
    val is_approved_post: Int,
)
