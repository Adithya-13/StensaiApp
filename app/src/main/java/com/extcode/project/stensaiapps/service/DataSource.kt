package com.extcode.project.stensaiapps.service

import com.extcode.project.stensaiapps.model.api.*
import com.extcode.project.stensaiapps.model.api.post.PostPengajuan
import com.extcode.project.stensaiapps.model.api.post.PostSaran
import com.extcode.project.stensaiapps.model.api.post.SignInStudent
import com.extcode.project.stensaiapps.model.api.post.SignInTeacher
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DataSource {

    @GET("post")
    fun magazine(): Call<MagazineResponse>

    @GET("event")
    fun event(): Call<EventResponse>

    @GET("pengajuan")
    fun pengajuan(): Call<PengajuanResponse>

    @GET("saran")
    fun saran(): Call<SaranResponse>

    @GET("siswa")
    fun student(): Call<AllStudentResponse>

    @POST("siswa/login")
    fun studentSignIn(@Body signInStudent: SignInStudent): Call<StudentResponse>

    @POST("guru/login")
    fun teacherSignIn(@Body signInTeacher: SignInTeacher): Call<TeacherResponse>

    @POST("pengajuan")
    fun postPengajuan(@Body postPengajuan: PostPengajuan): Call<PostPengajuanResponse>

    @POST("saran")
    fun postSaran(@Body postSaran: PostSaran): Call<PostSaranResponse>
}