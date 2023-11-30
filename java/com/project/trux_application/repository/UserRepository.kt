package com.project.trux_application.repository

import com.project.trux_application.api.ApiClient
import com.project.trux_application.api.ApiInterface
import com.project.trux_application.model.RegisterRequest
import com.project.trux_application.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserRepository {
    val apiClient= ApiClient.buildClient(ApiInterface::class.java)

    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return withContext(Dispatchers.IO){
            apiClient.registerUser(registerRequest)
        }
    }
}