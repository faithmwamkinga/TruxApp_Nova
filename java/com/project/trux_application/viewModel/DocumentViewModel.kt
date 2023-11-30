package com.project.trux_application.viewModel

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.trux_application.model.DocumentRequest
import com.project.trux_application.model.DocumentResponse
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.repository.DocumentRepository
import kotlinx.coroutines.launch
import java.io.InputStream

class DocumentViewModel : ViewModel() {
    var documentsLiveData = MutableLiveData<DocumentResponse?>()
    var documentPersonalLiveData = MutableLiveData<List<SavedDocuments>>()
    var documentCargoLiveData = MutableLiveData<List<SavedDocuments>>()
    var documentError = MutableLiveData<String>()
    private val documentRepository = DocumentRepository()


    fun postPersonalDocs(documentRequest: DocumentRequest, imagePath: String, authToken:String) {
        viewModelScope.launch {
            val response = documentRepository.postDocuments(documentRequest, imagePath, authToken)
            if (response.isSuccessful) {
                documentsLiveData.postValue(response.body())
            } else {
                documentError.postValue(response.errorBody()?.string())
            }
        }
    }

    fun ssaveDocumentToDb(documents: SavedDocuments){
        viewModelScope.launch {
            documentRepository.saveDocumentToDb(documents)
        }
    }
    fun fetchDocuments():LiveData<List<SavedDocuments>>{
       return documentRepository.fetchDocuments()
    }

}
