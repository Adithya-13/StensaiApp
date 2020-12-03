package com.extcode.project.stensaiapps.model.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostPengajuanResponse(

	@field:SerializedName("data")
	val data: PostPengajuanData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class PostPengajuanData(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pengajuan")
	val pengajuan: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("siswa_id")
	val siswaId: String? = null,

	@field:SerializedName("siswa_b")
	val siswaB: String? = null
) : Parcelable
