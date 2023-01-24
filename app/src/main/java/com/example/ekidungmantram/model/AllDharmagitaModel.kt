package com.example.ekidungmantram.model

data class AllDharmagitaModel(
    val id_post: Int,
    val id_kategori: Int,
    val id_tag: Int,
    val kategori: String,
    val nama_post: String,
    val nama_tag: String,
    val gambar: String,
    val bait_kidung: String,
    val bait_pupuh: String,
    val bait_sekar_agung: String,
    val bait_lagu: String,
)
