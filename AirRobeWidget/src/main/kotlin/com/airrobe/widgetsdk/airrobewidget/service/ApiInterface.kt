package com.airrobe.widgetsdk.airrobewidget.service

import com.airrobe.widgetsdk.airrobewidget.service.models.EmailCheckResponseModel
import com.airrobe.widgetsdk.airrobewidget.service.models.PriceEngineResponseModel
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/graphql")
    suspend fun getCategoryMapping(@Body body: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("/graphql")
    suspend fun emailCheck(@Body body: String): Response<EmailCheckResponseModel>

    @GET("/v1")
    suspend fun priceEngine(
        @Query("price") price: String,
        @Query("rrp") rrp: Double?,
        @Query("category") category: String,
        @Query("brand") brand: String?,
        @Query("material") material: String?
    ): Response<PriceEngineResponseModel>
}