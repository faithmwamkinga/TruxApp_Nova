package com.project.trux_application.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.project.trux_application.TruxApp
import com.project.trux_application.api.ApiClient
import com.project.trux_application.api.ApiInterface
import com.project.trux_application.database.DocumentsDb
import com.project.trux_application.model.DocumentRequest
import com.project.trux_application.model.DocumentResponse
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.utils.InputStreamRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.InputStream


class DocumentRepository{
     val apiInterface = ApiClient.buildClient(ApiInterface::class.java)
    val db = DocumentsDb.getDatabase(TruxApp.appContext)
    val documentsDao = db.documentsDao()




     fun createMultipartFromImage(imagePath: String, paramName: String): MultipartBody.Part {
        val file = File(imagePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, file.name,requestFile)
    }

    suspend fun postDocuments(documentRequest: DocumentRequest, imagePath: String, authToken:String): Response<DocumentResponse> {
        return withContext(Dispatchers.IO) {
            val image =  createMultipartFromImage(imagePath, "document_image")
            val response = apiInterface.uploadPersonalDocuments(
                referenceNumber = documentRequest.referencenumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                issueDate = documentRequest.issueDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                expiryDate = documentRequest.expiryDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                documentTYpe = documentRequest.documentType.toRequestBody("text/plain".toMediaTypeOrNull()),
                token = authToken,
                image = image
            )
            response
        }
    }

suspend fun saveDocumentToDb(documents: SavedDocuments){
    withContext(Dispatchers.IO){
        db.documentsDao().postPersonalDocuments(documents)
    }
}
    fun fetchDocuments():LiveData<List<SavedDocuments>>{
        return db.documentsDao().getPersonalDocuments()
    }




}