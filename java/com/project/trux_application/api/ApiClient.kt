package com.project.trux_application.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    var retrofit = Retrofit.Builder()
        .baseUrl("https://novabackend-89a7096e1507.herokuapp.com/")
        .client(buildOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> buildClient(apiInterface: Class<T>):T{
        return retrofit.create(apiInterface)
    }
    private fun buildOkHttpClient():OkHttpClient{
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()

    }


    }






