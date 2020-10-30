package com.extcode.project.stensaiapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeacherModel(
    var uid: String?,
    var email: String?,
    var username: String?,
    var nip: Long?,
    var status: Int = 1
) : Parcelable {
    constructor() : this("", "", "",0, 0)
}