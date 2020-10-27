package com.extcode.project.stensaiapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel(
    val titleTask : String?,
    val descriptionTask : String?
) : Parcelable