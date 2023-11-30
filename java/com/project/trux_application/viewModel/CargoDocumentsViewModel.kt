package com.project.trux_application.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.CargoDocsRequest
import com.project.trux_application.model.CargoDocsResponse
import com.project.trux_application.model.TruckDocsData
import com.project.trux_application.repository.CargoDocumentRepository
import kotlinx.coroutines.launch

class CargoDocumentsViewModel : ViewModel() {
        var cargoDocumentsLiveData = MutableLiveData<CargoDocsResponse?>()
        var cargoDocumentError = MutableLiveData<String>()
        private val cargoDocumentRepository = CargoDocumentRepository()


        fun postCargoDocs(cargoDocsRequest: CargoDocsRequest, imagePath: String, authToken:String) {
            viewModelScope.launch {
                val response = cargoDocumentRepository.postCargoDocuments(cargoDocsRequest, imagePath,authToken)
                if (response.isSuccessful) {
                    cargoDocumentsLiveData.postValue(response.body())
                } else {
                    cargoDocumentError.postValue(response.errorBody()?.string())
                }
            }
        }
    fun saveCargoDocumentToDb(cargoDocuments: CargoDocsData){
        viewModelScope.launch {
            cargoDocumentRepository.saveCargoDocumentToDb(cargoDocuments)
        }
    }
    fun fetchCargoDocuments(): LiveData<List<CargoDocsData>> {
        return cargoDocumentRepository.fetchCargoDocuments()
    }
}

