package com.extcode.project.stensaiapps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.extcode.project.stensaiapps.model.api.EventResponse
import com.extcode.project.stensaiapps.service.DataSource
import com.extcode.project.stensaiapps.service.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    private val eventResponse = MutableLiveData<EventResponse>()

    fun getEvent(): LiveData<EventResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProvider.providesHttpAdapter().create(DataSource::class.java).apply {
                event().enqueue(object : Callback<EventResponse> {
                    override fun onResponse(
                        call: Call<EventResponse>,
                        response: Response<EventResponse>
                    ) {
                        eventResponse.postValue(response.body())
                    }

                    override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                        Log.e(DashboardViewModel::class.simpleName, t.toString())
                    }
                })
            }
        }
        return eventResponse
    }

}