package com.extcode.project.stensaiapps.model.api.post

import com.google.gson.annotations.SerializedName

data class SignInStudent(

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("remember_me")
	val rememberMe: Boolean = true

)
