package com.example.ekidungmantram.model

data class AdminModel(
    val error : Boolean,
    val message: String,
    val id_admin: Int,
    val nama : String,
    val role : Int,
    val mobile_is_logged : Int,
    val is_approved: Int,
    val file : String,
)