package com.extcode.project.stensaiapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentModel(
    var uid: String?,
    var username: String?,
    var email: String?,
    var nis: Long?,
    var className: String?,
    var status: Int = 0
) : Parcelable {
    constructor() : this("", "", "", 0, "", 0)
}