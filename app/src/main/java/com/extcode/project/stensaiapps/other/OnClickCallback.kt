package com.extcode.project.stensaiapps.other

import com.extcode.project.stensaiapps.model.api.StudentData
import com.extcode.project.stensaiapps.model.api.TeacherData
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData

interface OnUnfinishedItemClickCallback {
    fun sendUnfinishedData(unfinishedData: UnfinishedData)
}

interface OnFinishedItemClickCallback {
    fun sendFinishedData(finishedData: FinishedData)
}

interface OnStudentNewMessageItemClickCallback {
    fun sendStudentData(studentData: StudentData)
}

interface OnTeacherNewMessageItemClickCallback {
    fun sendTeacherData(teacherData: TeacherData)
}