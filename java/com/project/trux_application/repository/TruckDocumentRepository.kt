package com.project.trux_application.repository

import androidx.lifecycle.LiveData
import com.project.trux_application.TruxApp
import com.project.trux_application.api.ApiClient
import com.project.trux_application.api.ApiInterface
import com.project.trux_application.database.DocumentsDb
import com.project.trux_application.database.TruckDocumentDb
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsData
import com.project.trux_application.model.TruckDocsRequest
import com.project.trux_application.model.TruckDocsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class TruckDocumentRepository {
    val apiInterface = ApiClient.buildClient(ApiInterface::class.java)
    val db = TruckDocumentDb.getDatabase(TruxApp.appContext)
    val truckDocsDao = db.truckDocsDao()

    fun createMultipartFromImage(imagePath: String, paramName: String): MultipartBody.Part {
        val file = File(imagePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, file.name,requestFile)
    }

    suspend fun postTruckDocuments(truckDocsRequest: TruckDocsRequest, imagePath: String, authToken:String): Response<TruckDocsResponse> {
        return withContext(Dispatchers.IO) {
            val image =  createMultipartFromImage(imagePath, "document_image")
            val response = apiInterface.uploadTruckDocuments(
                referenceNumber = truckDocsRequest.referenceNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                issueDate = truckDocsRequest.issueDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                expiryDate = truckDocsRequest.expiryDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                documentType = truckDocsRequest.documentType.toRequestBody("text/plain".toMediaTypeOrNull()),
                token = authToken,
                image = image
            )
            response
        }
    }

    suspend fun saveTruckDocumentToDb(documents: TruckDocsData){
        withContext(Dispatchers.IO){
            db.truckDocsDao().postTruckDocuments(documents)
        }
    }
    fun fetchTruckDocuments(): LiveData<List<TruckDocsData>> {
        return db.truckDocsDao().getTruckDocuments()
    }

}