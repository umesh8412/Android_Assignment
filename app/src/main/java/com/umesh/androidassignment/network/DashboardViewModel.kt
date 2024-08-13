package com.umesh.androidassignment.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val responseData = MutableLiveData<DashboardResponse>()
    val loading = MutableLiveData<Boolean>()
    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String> = _failure

    fun loadDashboardData() = viewModelScope.launch {
        loading.postValue(true)
        try {
            val response = repository.fetchDashboardData()
            if (response.isSuccessful) {
                responseData.postValue(response.body())
                Log.d("DashboardViewModel", "API response: ${response.body()}")
            } else {
                Log.e("DashboardViewModel", "Error: ${response.message()}")
            }
        } catch (e: Exception) {
            // Log exception
            Log.e("DashboardViewModel", "Exception: $e")
        } finally {
            loading.postValue(false)
        }
    }

}