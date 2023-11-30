package com.project.trux_application.repository

import androidx.lifecycle.LiveData
import com.project.trux_application.TruxApp
import com.project.trux_application.api.ApiClient
import com.project.trux_application.api.ApiInterface
import com.project.trux_application.database.CargoDocsDb
import com.project.trux_application.model.CargoDocsData
import com.project.trux_application.model.CargoDocsRequest
import com.project.trux_application.model.CargoDocsResponse
import com.project.trux_application.model.TruckDocsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File


class CargoDocumentRepository {
        val apiInterface = ApiClient.buildClient(ApiInterface::class.java)
        val database = CargoDocsDb.getDatabase(TruxApp.appContext)
        val cargoDocsDao = database.cargoDocsDao()





        fun createMultipartFromImage(imagePath: String, paramName: String): MultipartBody.Part {
            val file = File(imagePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(paramName, file.name,requestFile)
        }

        suspend fun postCargoDocuments(cargoDocsRequest:CargoDocsRequest, imagePath: String, authToken:String): Response<CargoDocsResponse> {
            return withContext(Dispatchers.IO) {
                val image =  createMultipartFromImage(imagePath, "document_image")
                val response = apiInterface.uploadCargoDocuments(
                    referenceNumber = cargoDocsRequest.referenceNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                    issueDate = cargoDocsRequest.issueDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                    expiryDate = cargoDocsRequest.expiryDate.toRequestBody("text/plain".toMediaTypeOrNull()),
                    documentTYpe = cargoDocsRequest.documentType.toRequestBody("text/plain".toMediaTypeOrNull()),
                    cargoTones = cargoDocsRequest.cargoTones.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    cargo = cargoDocsRequest.cargo.toRequestBody("text/plain".toMediaTypeOrNull()),
                    token = authToken,
                    image = image
                )
                response
            }
        }
    suspend fun saveCargoDocumentToDb(documents: CargoDocsData){
        withContext(Dispatchers.IO){
            database.cargoDocsDao().postCargoDocuments(documents)
        }
    }
    fun fetchCargoDocuments(): LiveData<List<CargoDocsData>> {
        return database.cargoDocsDao().getCargoDocuments()
    }





}