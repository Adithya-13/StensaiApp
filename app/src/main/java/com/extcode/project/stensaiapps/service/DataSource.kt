package com.extcode.project.stensaiapps.service

import com.extcode.project.stensaiapps.model.api.MagazineResponse
import retrofit2.Call
import retrofit2.http.GET

interface DataSource {

    //Mading
    @GET("post")
    fun magazine(): Call<MagazineResponse>

}