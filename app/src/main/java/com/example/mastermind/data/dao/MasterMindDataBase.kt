package com.example.mastermind.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.mastermind.data.entity.GameEntity
import com.example.mastermind.data.util.Converters
import com.example.mastermind.data.dao.GameDao

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MastermindDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile private var INSTANCE: MastermindDatabase? = null

        fun get(context: Context): MastermindDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MastermindDatabase::class.java,
                    "mastermind.db"
                ).build().also { INSTANCE = it }
            }
    }
}

