package com.umesh.androidassignment.network

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("dashboardNew")
    suspend fun getDashboardData(): Response<DashboardResponse>
}