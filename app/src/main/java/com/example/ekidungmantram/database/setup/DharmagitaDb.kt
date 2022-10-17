package com.example.ekidungmantram.database.setup

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ekidungmantram.Constant
import com.example.ekidungmantram.database.dao.DharmagitaDao
import com.example.ekidungmantram.database.dao.YadnyaDao
import com.example.ekidungmantram.database.data.Dharmagita
import com.example.ekidungmantram.database.data.Yadnya

@Database(
    entities     = [Dharmagita::class],
    version      = 3,
    exportSchema = false
)
abstract class DharmagitaDb : RoomDatabase() {
    abstract fun dharmagitaDao(): DharmagitaDao
}