package com.project.trux_application.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.trux_application.model.LoginRequest
import kotlinx.coroutines.launch
import com.project.trux_application.repository.LoginRepository
import com.project.trux_application.model.LoginResponse // Make sure to import LoginResponse

class LoginViewModel : ViewModel() {
    val loginRepo = LoginRepository()
    val loginLiveData = MutableLiveData<LoginResponse>() // Use LoginResponse here
    val errLiveData = MutableLiveData<String>()

    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            val response = loginRepo.loginUser(loginRequest)

            if (response.isSuccessful) {
                loginLiveData.postValue(response.body())
            } else {
                errLiveData.postValue(response.errorBody()?.string())
            }
        }
    }
}



