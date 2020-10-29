package com.extcode.project.stensaiapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extcode.project.stensaiapps.model.api.MagazineResponse
import com.extcode.project.stensaiapps.service.DataSource
import com.extcode.project.stensaiapps.service.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagazineViewModel : ViewModel() {

    private val magazineResponse = MutableLiveData<MagazineResponse>()

    fun getMagazines(): LiveData<MagazineResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                magazine().enqueue(object : Callback<MagazineResponse> {
                    override fun onResponse(
                        call: Call<MagazineResponse>,
                        response: Response<MagazineResponse>
                    ) {
                        val items = response.body()
                        magazineResponse.postValue(items)
                    }

                    override fun onFailure(call: Call<MagazineResponse>, t: Throwable) {
                        Log.d(MagazineViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return magazineResponse
    }
}