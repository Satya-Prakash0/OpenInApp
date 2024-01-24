package com.app.openinapp.db

import com.app.openinapp.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface OpenInAppApi {

    @GET("dashboardNew")
    suspend fun getDashboardData(@Header("Authorization") token: String): Response<DataResponse>

}