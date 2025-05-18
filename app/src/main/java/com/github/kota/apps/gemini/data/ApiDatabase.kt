package com.github.kota.apps.gemini.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.kota.apps.gemini.model.ApiKeyEntity

@Database(entities = [ApiKeyEntity::class], version = 2, exportSchema = false) // 🔹 version = 2 に変更！
abstract class ApiDatabase : RoomDatabase() {
    abstract fun apiKeyDao(): ApiKeyDao

    companion object {
        @Volatile
        private var INSTANCE: ApiDatabase? = null

        fun getDatabase(context: Context): ApiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApiDatabase::class.java,
                    "api_database"
                )
                    .addMigrations(MIGRATION_1_2) // 🔹 マイグレーションを追加
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}
