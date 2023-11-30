package com.project.trux_application.repository

import com.project.trux_application.api.ApiClient
import com.project.trux_application.api.ApiInterface
import com.project.trux_application.model.LoginRequest
import com.project.trux_application.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    val apiClient= ApiClient.buildClient(ApiInterface::class.java)

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return withContext(Dispatchers.IO){
            apiClient.loginUser(loginRequest)
        }
    }
}


