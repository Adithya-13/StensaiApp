package com.extcode.project.stensaiapps.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.TASK_DATABASE_NAME
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData

@Database(entities = [UnfinishedData::class, FinishedData::class], version = 4, exportSchema = true)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun unfinishedTaskDao(): UnfinishedTaskDao
    abstract fun finishedTaskDao(): FinishedTaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    TASK_DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}