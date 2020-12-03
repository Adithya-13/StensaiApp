package com.extcode.project.stensaiapps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.extcode.project.stensaiapps.db.StensaiDatabase
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.ScheduleData
import com.extcode.project.stensaiapps.model.db.UnfinishedData

class TaskViewModel : ViewModel() {

    fun queryAllUnfinishedTask(context: Context): LiveData<List<UnfinishedData>> {
        val db = StensaiDatabase.getInstance(context)
        return db.unfinishedTaskDao().queryAllUnfinishedTask()
    }

    fun insertUnfinishedTask(context: Context, unfinishedData: UnfinishedData): Long {
        val db = StensaiDatabase.getInstance(context)
        return db.unfinishedTaskDao().insertUnfinishedTask(unfinishedData)
    }

    fun deleteUnfinishedTask(context: Context, id: Int): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.unfinishedTaskDao().deleteUnfinishedTask(id)
    }

    fun deleteAllUnfinishedTask(context: Context): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.unfinishedTaskDao().deleteUnfinishedAll()
    }

    fun queryAllFinishedTask(context: Context): LiveData<List<FinishedData>> {
        val db = StensaiDatabase.getInstance(context)
        return db.finishedTaskDao().queryAllFinishedTask()
    }

    fun insertFinishedTask(context: Context, finishedData: FinishedData): Long {
        val db = StensaiDatabase.getInstance(context)
        return db.finishedTaskDao().insertFinishedTask(finishedData)
    }

    fun deleteFinishedTask(context: Context, id: Int): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.finishedTaskDao().deleteFinishedTask(id)
    }

    fun deleteAllFinishedTask(context: Context): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.finishedTaskDao().deleteFinishedAll()
    }

    fun queryAllSchedules(context: Context): LiveData<List<ScheduleData>> {
        val db = StensaiDatabase.getInstance(context)
        return db.scheduleDao().queryAllSchedules()
    }

    fun insertSchedule(context: Context, scheduleData: ScheduleData): Long {
        val db = StensaiDatabase.getInstance(context)
        return db.scheduleDao().insertSchedule(scheduleData)
    }

    fun deleteSchedule(context: Context, id: Int): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.scheduleDao().deleteSchedule(id)
    }

    fun deleteAllSchedules(context: Context): Int {
        val db = StensaiDatabase.getInstance(context)
        return db.scheduleDao().deleteAllSchedules()
    }

}