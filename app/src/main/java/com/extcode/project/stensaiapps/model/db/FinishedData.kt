package com.extcode.project.stensaiapps.model.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_DATE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_DESCRIPTION
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_ID
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_TABLE_NAME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_TIME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_TITLE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.FINISHED_URGENCY
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = FINISHED_TABLE_NAME)
data class FinishedData(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = FINISHED_ID) val id: Int = 0,
    @ColumnInfo(name = FINISHED_TITLE) var title: String?,
    @ColumnInfo(name = FINISHED_DESCRIPTION) var description: String?,
    @ColumnInfo(name = FINISHED_DATE) var date: String?,
    @ColumnInfo(name = FINISHED_TIME) var time: String?,
    @ColumnInfo(name = FINISHED_URGENCY) var priority: Int = 0

) : Parcelable