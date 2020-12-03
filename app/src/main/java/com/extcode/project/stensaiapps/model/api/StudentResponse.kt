package com.extcode.project.stensaiapps.model.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentResponse(

	@field:SerializedName("data")
	val data: StudentData? = null,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class StudentData(

	@field:SerializedName("jk")
	val jk: String? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("nama_ibu")
	val namaIbu: String? = null,

	@field:SerializedName("agama")
	val agama: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("tmp_lahir")
	val tmpLahir: String? = null,

	@field:SerializedName("tgl_lahir")
	val tglLahir: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("anak_ke")
	val anakKe: String? = null,

	@field:SerializedName("nama_ayah")
	val namaAyah: String? = null,

	@field:SerializedName("nis")
	val nis: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("kelas_id")
	val kelasId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable
