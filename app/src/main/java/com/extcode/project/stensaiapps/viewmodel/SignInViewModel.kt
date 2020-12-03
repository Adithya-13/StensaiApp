package com.extcode.project.stensaiapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extcode.project.stensaiapps.model.api.StudentData
import com.extcode.project.stensaiapps.model.api.StudentResponse
import com.extcode.project.stensaiapps.model.api.TeacherData
import com.extcode.project.stensaiapps.model.api.TeacherResponse
import com.extcode.project.stensaiapps.model.api.post.SignInStudent
import com.extcode.project.stensaiapps.model.api.post.SignInTeacher
import com.extcode.project.stensaiapps.service.DataSource
import com.extcode.project.stensaiapps.service.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {

    fun setStudentSignIn(email: String, password: String): LiveData<StudentResponse> {
        val studentResponse = MutableLiveData<StudentResponse>()
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                studentSignIn(SignInStudent(email, password)).enqueue(object :
                    Callback<StudentResponse> {
                    override fun onResponse(
                        call: Call<StudentResponse>,
                        response: Response<StudentResponse>
                    ) {
                        when {
                            response.code() == 200 -> {
                                studentResponse.postValue(response.body())
                                Log.d("anjay", response.body().toString())
                            }
                            response.code() == 404 -> {
                                studentResponse.postValue(
                                    StudentResponse(
                                        StudentData(),
                                        false,
                                        "Siswa tidak ditemukan"
                                    )
                                )

                            }
                            else -> {
                                studentResponse.postValue(
                                    StudentResponse(
                                        StudentData(),
                                        false,
                                        "Gagal Login"
                                    )
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                        Log.d(SignInViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return studentResponse
    }

    fun setTeacherSignIn(email: String, password: String): LiveData<TeacherResponse> {
        val teacherResponse = MutableLiveData<TeacherResponse>()
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                teacherSignIn(SignInTeacher(email, password)).enqueue(object :
                    Callback<TeacherResponse> {
                    override fun onResponse(
                        call: Call<TeacherResponse>,
                        response: Response<TeacherResponse>
                    ) {
                        when {
                            response.code() == 200 -> {
                                Log.d("anjay", response.body().toString())
                                teacherResponse.postValue(response.body())
                            }
                            response.code() == 404 -> {
                                teacherResponse.postValue(
                                    TeacherResponse(
                                        TeacherData(),
                                        false,
                                        "Guru tidak ditemukan"
                                    )
                                )
                            }
                            else -> {
                                teacherResponse.postValue(
                                    TeacherResponse(
                                        TeacherData(),
                                        false,
                                        "Gagal Login"
                                    )
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<TeacherResponse>, t: Throwable) {
                        Log.d(SignInViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return teacherResponse
    }

}