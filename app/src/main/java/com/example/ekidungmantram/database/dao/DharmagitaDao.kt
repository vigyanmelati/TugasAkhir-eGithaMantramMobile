package com.example.ekidungmantram.database.dao

import androidx.room.*
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.database.data.Yadnya

@Dao
interface DharmagitaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmarkedDharmagita(dharmagita: Dharmagita)

    @Delete
    suspend fun deleteBookmarkedDharmagita(dharmagita: Dharmagita)

    @Query("DELETE FROM dharmagitabookmarked WHERE id_dharmagita = :id")
    suspend fun deleteById(id: Int)

    @Query("Select * from dharmagitabookmarked where id_dharmagita = :id")
    suspend fun fetch(id: Int) : Dharmagita

    @Query("Select * from dharmagitabookmarked order by id DESC")
    suspend fun getAllBookmarkedDharmagita() : List<Dharmagita>
}