package com.extcode.project.stensaiapps.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_DATE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_TABLE_NAME
import com.extcode.project.stensaiapps.model.db.UnfinishedData

@Dao
interface UnfinishedTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUnfinishedTask(unfinishedData: UnfinishedData): Long

    @Query("SELECT * FROM $UNFINISHED_TABLE_NAME ORDER BY $UNFINISHED_DATE ASC")
    fun queryAllUnfinishedTask(): LiveData<List<UnfinishedData>>

    @Query("DELETE FROM $UNFINISHED_TABLE_NAME WHERE id = :id")
    fun deleteUnfinishedTask(id: Int): Int

    @Query("DELETE FROM $UNFINISHED_TABLE_NAME")
    fun deleteUnfinishedAll(): Int
}