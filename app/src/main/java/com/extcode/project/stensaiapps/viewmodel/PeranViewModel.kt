package com.extcode.project.stensaiapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extcode.project.stensaiapps.model.api.*
import com.extcode.project.stensaiapps.model.api.post.PostPengajuan
import com.extcode.project.stensaiapps.model.api.post.PostSaran
import com.extcode.project.stensaiapps.service.DataSource
import com.extcode.project.stensaiapps.service.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PeranViewModel : ViewModel() {

    private val pengajuanResponse = MutableLiveData<PengajuanResponse>()
    private val saranResponse = MutableLiveData<SaranResponse>()
    private val postPengajuanResponse = MutableLiveData<PostPengajuanResponse>()
    private val postSaranResponse = MutableLiveData<PostSaranResponse>()
    private val allStudentResponse = MutableLiveData<AllStudentResponse>()

    fun getPengajuan(): LiveData<PengajuanResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                pengajuan().enqueue(object : Callback<PengajuanResponse> {
                    override fun onResponse(
                        call: Call<PengajuanResponse>,
                        response: Response<PengajuanResponse>
                    ) {
                        val items = response.body()
                        pengajuanResponse.postValue(items)
                    }

                    override fun onFailure(call: Call<PengajuanResponse>, t: Throwable) {
                        Log.d(PeranViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return pengajuanResponse
    }

    fun getSaran(): LiveData<SaranResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                saran().enqueue(object : Callback<SaranResponse> {
                    override fun onResponse(
                        call: Call<SaranResponse>,
                        response: Response<SaranResponse>
                    ) {
                        val items = response.body()
                        saranResponse.postValue(items)
                    }

                    override fun onFailure(call: Call<SaranResponse>, t: Throwable) {
                        Log.d(PeranViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return saranResponse
    }

    fun setPostPengajuan(pengajuan: String, deskripsi: String, siswa_id: String, siswa_b: String) {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                postPengajuan(
                    PostPengajuan(
                        pengajuan,
                        deskripsi,
                        siswa_id,
                        siswa_b
                    )
                ).enqueue(object : Callback<PostPengajuanResponse> {
                    override fun onResponse(
                        call: Call<PostPengajuanResponse>,
                        response: Response<PostPengajuanResponse>
                    ) {
                        postPengajuanResponse.postValue(response.body())
                    }

                    override fun onFailure(call: Call<PostPengajuanResponse>, t: Throwable) {
                        Log.d(PeranViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
    }

    fun setPostSaran(event_id: String, deskripsi: String, siswa_id: String, siswa_b: String) {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                postSaran(PostSaran(event_id, deskripsi, siswa_id, siswa_b)).enqueue(object :
                    Callback<PostSaranResponse> {
                    override fun onResponse(
                        call: Call<PostSaranResponse>,
                        response: Response<PostSaranResponse>
                    ) {
                        postSaranResponse.postValue(response.body())
                    }

                    override fun onFailure(call: Call<PostSaranResponse>, t: Throwable) {
                        Log.d(PeranViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
    }

    fun setSiswa() {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                student().enqueue(object :
                    Callback<AllStudentResponse> {
                    override fun onResponse(
                        call: Call<AllStudentResponse>,
                        response: Response<AllStudentResponse>
                    ) {
                        allStudentResponse.postValue(response.body())
                    }

                    override fun onFailure(call: Call<AllStudentResponse>, t: Throwable) {
                        Log.d(PeranViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
    }

    fun getSiswa(): LiveData<AllStudentResponse> {
        return allStudentResponse
    }

    fun getPostPengajuan(): LiveData<PostPengajuanResponse> {
        return postPengajuanResponse
    }

    fun getPostSaran(): LiveData<PostSaranResponse> {
        return postSaranResponse
    }
}