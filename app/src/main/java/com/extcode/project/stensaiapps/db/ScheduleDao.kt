package com.extcode.project.stensaiapps.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_TABLE_NAME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_TIME
import com.extcode.project.stensaiapps.model.db.ScheduleData

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(scheduleData: ScheduleData): Long

    @Query("SELECT * FROM $SCHEDULE_TABLE_NAME ORDER BY $SCHEDULE_TIME ASC")
    fun queryAllSchedules(): LiveData<List<ScheduleData>>

    @Query("DELETE FROM $SCHEDULE_TABLE_NAME WHERE id = :id")
    fun deleteSchedule(id: Int): Int

    @Query("DELETE FROM $SCHEDULE_TABLE_NAME")
    fun deleteAllSchedules(): Int
}