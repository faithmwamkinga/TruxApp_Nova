package com.project.trux_application.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData
import com.project.trux_application.model.TruckDocsRequest
import com.project.trux_application.model.TruckDocsResponse
import com.project.trux_application.repository.TruckDocumentRepository
import kotlinx.coroutines.launch

class TruckDocumentsViewModel : ViewModel(){
    var truckdocsLiveData = MutableLiveData<TruckDocsResponse?>()
    var documentTruckLiveData = MutableLiveData<List<TruckDocsData>>()
    var truckdocsError = MutableLiveData<String>()
    private val documentRepository = TruckDocumentRepository()


    fun postTruckDocs(truckDocsRequest: TruckDocsRequest, imagePath: String,authToken:String) {
        viewModelScope.launch {
            val response = documentRepository.postTruckDocuments(truckDocsRequest, imagePath,authToken)
            if (response.isSuccessful) {
                truckdocsLiveData.postValue(response.body())
            } else {
                truckdocsError.postValue(response.errorBody()?.string())
            }
        }
    }
    fun saveTruckDocumentToDb(truckDocuments: TruckDocsData){
        viewModelScope.launch {
            documentRepository.saveTruckDocumentToDb(truckDocuments)
        }
    }
    fun fetchTruckDocuments(): LiveData<List<TruckDocsData>> {
        return documentRepository.fetchTruckDocuments()
    }


}