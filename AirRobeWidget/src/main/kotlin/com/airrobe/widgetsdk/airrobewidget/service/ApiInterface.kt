package com.airrobe.widgetsdk.airrobewidget.service

import com.airrobe.widgetsdk.airrobewidget.service.models.EmailCheckResponseModel
import com.airrobe.widgetsdk.airrobewidget.service.models.PriceEngineResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/graphql")
    suspend fun getCategoryMapping(@Body body: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("/graphql")
    fun emailCheck(@Body body: String): Call<String>

    @GET("/v1")
    fun priceEngine(
        @Query("price") price: Float,
        @Query("rrp") rrp: Float?,
        @Query("category") category: String,
        @Query("brand") brand: String?,
        @Query("material") material: String?
    ): Call<PriceEngineResponseModel>
}