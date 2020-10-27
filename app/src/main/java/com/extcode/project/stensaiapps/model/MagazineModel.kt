package com.extcode.project.stensaiapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MagazineModel(
    val avatar : String?,
    val title : String?,
    val picture : String?,
    val description : String?
) : Parcelable