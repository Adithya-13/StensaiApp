package com.extcode.project.stensaiapps.db

import android.provider.BaseColumns

object DatabaseContract {

    class TaskColumn : BaseColumns {
        companion object{
            const val TASK_DATABASE_NAME = "taskDatabase"

            const val UNFINISHED_TABLE_NAME = "unfinishedTable"
            const val UNFINISHED_TITLE = "unfinishedTitle"
            const val UNFINISHED_DESCRIPTION = "unfinishedDescription"
            const val UNFINISHED_DATE = "unfinishedDate"
            const val UNFINISHED_TIME = "unfinishedTime"

            const val FINISHED_TABLE_NAME = "finishedTable"
            const val FINISHED_ID = "finishedId"
            const val FINISHED_TITLE = "finishedTitle"
            const val FINISHED_DESCRIPTION = "finishedDescription"
            const val FINISHED_DATE = "finishedDate"
            const val FINISHED_TIME = "finishedTime"
        }
    }
}