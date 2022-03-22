package com.airrobe.widgetsdk.airrobewidget.service

import com.airrobe.widgetsdk.airrobewidget.service.models.PriceEngineResponseModel
import retrofit2.Call
import retrofit2.http.*

internal interface AirRobeApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/graphql")
    fun mainGraphQL(@Body body: String): Call<String>

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