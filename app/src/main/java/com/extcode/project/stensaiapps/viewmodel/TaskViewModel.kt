package com.extcode.project.stensaiapps.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.extcode.project.stensaiapps.db.TaskDatabase
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData

class TaskViewModel : ViewModel() {

    fun queryAllUnfinishedTask(context: Context): LiveData<List<UnfinishedData>> {
        val db = TaskDatabase.getInstance(context)
        return db.unfinishedTaskDao().queryAllUnfinishedTask()
    }

    fun insertUnfinishedTask(context: Context, unfinishedData: UnfinishedData): Long {
        val db = TaskDatabase.getInstance(context)
        return db.unfinishedTaskDao().insertUnfinishedTask(unfinishedData)
    }

    fun deleteUnfinishedTask(context: Context, id: Int): Int {
        val db = TaskDatabase.getInstance(context)
        return db.unfinishedTaskDao().deleteUnfinishedTask(id)
    }

    fun deleteAllUnfinishedTask(context: Context): Int {
        val db = TaskDatabase.getInstance(context)
        return db.unfinishedTaskDao().deleteUnfinishedAll()
    }

    fun queryAllFinishedTask(context: Context): LiveData<List<FinishedData>> {
        val db = TaskDatabase.getInstance(context)
        return db.finishedTaskDao().queryAllFinishedTask()
    }

    fun insertFinishedTask(context: Context, finishedData: FinishedData): Long {
        val db = TaskDatabase.getInstance(context)
        return db.finishedTaskDao().insertFinishedTask(finishedData)
    }

    fun deleteFinishedTask(context: Context, id: Int): Int {
        val db = TaskDatabase.getInstance(context)
        return db.finishedTaskDao().deleteFinishedTask(id)
    }

    fun deleteAllFinishedTask(context: Context): Int {
        val db = TaskDatabase.getInstance(context)
        return db.finishedTaskDao().deleteFinishedAll()
    }

}