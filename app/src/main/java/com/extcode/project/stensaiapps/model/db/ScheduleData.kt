package com.extcode.project.stensaiapps.model.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_DAY_CODE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_DESCRIPTION
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_TABLE_NAME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_TIME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.SCHEDULE_TITLE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = SCHEDULE_TABLE_NAME)
data class ScheduleData(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = SCHEDULE_TITLE) var title: String?,
    @ColumnInfo(name = SCHEDULE_DESCRIPTION) var description: String?,
    @ColumnInfo(name = SCHEDULE_DAY_CODE) var dayCode: Int?,
    @ColumnInfo(name = SCHEDULE_TIME) var time: String?
) : Parcelable