package com.airrobe.widgetsdk.airrobewidget.service

import com.airrobe.widgetsdk.airrobewidget.BuildConfig
import com.airrobe.widgetsdk.airrobewidget.config.Constants
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

private class BearerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().build()
        return chain.proceed(newRequest)
    }
}

private class UserAgentInterceptor : Interceptor {
    // For example: 'airrobeWidget/1.0.0 (Android 5.1.1)'
    val userHeaderString: String = "airrobeWidget/${BuildConfig.VERSION_NAME} (Android ${android.os.Build.VERSION.RELEASE})"
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("User-Agent", this.userHeaderString).build()
        return chain.proceed(newRequest)
    }
}

object AirRobeApiService {
    val categoryMappingService: ApiInterface by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(300, TimeUnit.SECONDS)
        httpClient.writeTimeout(300, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(BearerInterceptor())
        httpClient.addInterceptor(UserAgentInterceptor())

        Retrofit
            .Builder()
            .baseUrl(Constants.AIRROBE_CONNECTOR_PRODUCTION)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ApiInterface::class.java)
    }

    val priceEngineService: ApiInterface by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(300, TimeUnit.SECONDS)
        httpClient.writeTimeout(300, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(BearerInterceptor())
        httpClient.addInterceptor(UserAgentInterceptor())

        Retrofit
            .Builder()
            .baseUrl(Constants.PRICE_ENGINE_HOST)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
            .create(ApiInterface::class.java)
    }

    val emailCheckService: ApiInterface by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(300, TimeUnit.SECONDS)
        httpClient.writeTimeout(300, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(BearerInterceptor())
        httpClient.addInterceptor(UserAgentInterceptor())

        Retrofit
            .Builder()
            .baseUrl(Constants.EMAIL_CHECK_HOST)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ApiInterface::class.java)
    }
}