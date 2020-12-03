package com.extcode.project.stensaiapps.model.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_DATE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_DESCRIPTION
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_TABLE_NAME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_TIME
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_TITLE
import com.extcode.project.stensaiapps.db.DatabaseContract.TaskColumn.Companion.UNFINISHED_URGENCY
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = UNFINISHED_TABLE_NAME)
data class UnfinishedData(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = UNFINISHED_TITLE) var title: String?,
    @ColumnInfo(name = UNFINISHED_DESCRIPTION) var description: String?,
    @ColumnInfo(name = UNFINISHED_DATE) var date: String?,
    @ColumnInfo(name = UNFINISHED_TIME) var time: String?,
    @ColumnInfo(name = UNFINISHED_URGENCY) var priority: Int = 0
) : Parcelable