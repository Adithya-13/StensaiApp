package com.extcode.project.stensaiapps.model.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SaranResponse(

	@field:SerializedName("data")
	val data: List<SaranItem>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class SaranItem(

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

	@field:SerializedName("siswa_b")
	val siswaB: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("event_id")
	val eventId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("anak_ke")
	val anakKe: String? = null,

	@field:SerializedName("nama_ayah")
	val namaAyah: String? = null,

	@field:SerializedName("nis")
	val nis: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("remember_token")
	val rememberToken: Boolean? = null,

	@field:SerializedName("kelas_id")
	val kelasId: String? = null,

	@field:SerializedName("siswa_id")
	val siswaId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable
