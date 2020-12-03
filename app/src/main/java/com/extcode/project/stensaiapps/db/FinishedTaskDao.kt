package com.extcode.project.stensaiapps.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_DATE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_ID
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_TABLE_NAME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_URGENCY
import com.extcode.project.stensaiapps.model.db.FinishedData

@Dao
interface FinishedTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinishedTask(finishedData: FinishedData): Long

    @Query("SELECT * FROM $FINISHED_TABLE_NAME ORDER BY $FINISHED_URGENCY DESC")
    fun queryAllFinishedTask(): LiveData<List<FinishedData>>

    @Query("DELETE FROM $FINISHED_TABLE_NAME WHERE $FINISHED_ID = :id")
    fun deleteFinishedTask(id: Int): Int

    @Query("DELETE FROM $FINISHED_TABLE_NAME")
    fun deleteFinishedAll(): Int

}