package com.github.kota.apps.gemini.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.kota.apps.gemini.model.MessageEntity

@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "message_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // chatName を NULL 許可にする（デフォルト値は INSERT 時に指定する）
                database.execSQL("ALTER TABLE messages ADD COLUMN chatName TEXT DEFAULT NULL")

                // 既存のデータの chatName を 'kota' に更新
                database.execSQL("UPDATE messages SET chatName = 'kota' WHERE chatName IS NULL")
            }
        }
    }
}
