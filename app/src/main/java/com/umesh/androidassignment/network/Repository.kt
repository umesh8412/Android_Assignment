package com.umesh.androidassignment.network

import android.util.Log
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun fetchDashboardData() = apiInterface.getDashboardData()


}