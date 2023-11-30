package com.project.trux_application.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.trux_application.model.RegisterRequest
import com.project.trux_application.model.RegisterResponse
import com.project.trux_application.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel:ViewModel(){
    val userRepo= UserRepository()
    val regLiveData= MutableLiveData<RegisterResponse>()
    val errLiveData=MutableLiveData<String>()


    fun registerUser(registerRequest: RegisterRequest){
        viewModelScope.launch {
            val response = userRepo.registerUser(registerRequest)

            if (response.isSuccessful){
                regLiveData.postValue(response.body())
            }
            else{
                errLiveData.postValue(response.errorBody()?.string())
            }
        }
    }
}