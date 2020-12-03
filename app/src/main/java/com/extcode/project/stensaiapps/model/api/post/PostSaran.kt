package com.extcode.project.stensaiapps.model.api.post

import com.google.gson.annotations.SerializedName

data class PostSaran(

	@field:SerializedName("event_id")
	val event_id: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("siswa_id")
	val siswa_id: String? = null,

	@field:SerializedName("siswa_b")
	val siswa_b: String? = null

)
