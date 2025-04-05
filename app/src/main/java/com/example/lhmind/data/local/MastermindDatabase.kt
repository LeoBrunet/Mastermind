package com.example.lhmind.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lhmind.data.local.converter.Converters
import com.example.lhmind.data.local.dao.AttemptDao
import com.example.lhmind.data.local.dao.FeedbackDao
import com.example.lhmind.data.local.dao.GameDao
import com.example.lhmind.data.local.dao.PlayerDao
import com.example.lhmind.data.local.entity.AttemptEntity
import com.example.lhmind.data.local.entity.FeedbackEntity
import com.example.lhmind.data.local.entity.GameEntity
import com.example.lhmind.data.local.entity.PlayerEntity

@Database(
    entities = [
        GameEntity::class,
        AttemptEntity::class,
        FeedbackEntity::class,
        PlayerEntity::class,
        // Add other entities here
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MastermindDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun attemptDao(): AttemptDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun playerDao(): PlayerDao
    // Add other DAOs here

    companion object {
        private const val DATABASE_NAME = "mastermind_database"

        @Volatile
        private var INSTANCE: MastermindDatabase? = null

        fun getInstance(context: Context): MastermindDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MastermindDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}