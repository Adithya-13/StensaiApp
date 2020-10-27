package com.extcode.project.stensaiapps.other

import com.extcode.project.stensaiapps.model.StudentModel
import com.extcode.project.stensaiapps.model.TeacherModel
import com.extcode.project.stensaiapps.model.db.FinishedData
import com.extcode.project.stensaiapps.model.db.UnfinishedData

interface OnUnfinishedItemClickCallback {
    fun sendUnfinishedData(unfinishedData: UnfinishedData)
}

interface OnFinishedItemClickCallback {
    fun sendFinishedData(finishedData: FinishedData)
}

interface OnStudentNewMessageItemClickCallback{
    fun sendStudentData(studentModel: StudentModel)
}

interface OnTeacherNewMessageItemClickCallback{
    fun sendTeacherData(teacherModel: TeacherModel)
}