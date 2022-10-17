package com.example.ekidungmantram.database.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "dharmagitabookmarked", indices = [Index(value = ["id_dharmagita"], unique = true)])
data class Dharmagita(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val id_dharmagita: Int,
    val id_tag: Int,
    val nama_post: String,
    val gambar: String
){
    constructor() : this(0, 0,0 , "", "")
}
