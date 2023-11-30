package com.project.trux_application.api


import com.google.gson.annotations.SerializedName
import com.project.trux_application.model.CargoDocsResponse
import com.project.trux_application.model.DocumentRequest
import com.project.trux_application.model.DocumentResponse
import com.project.trux_application.model.LoginRequest
import com.project.trux_application.model.LoginResponse
import com.project.trux_application.model.RegisterRequest
import com.project.trux_application.model.RegisterResponse
import com.project.trux_application.model.SavedDocuments
import com.project.trux_application.model.TruckDocsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    companion object{
        const val apiKey = "828226852764488"
    }
    @POST("api/driver/login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/driver/signup/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest):Response<RegisterResponse>

    @Multipart
    @POST("api/personal-documents/")
    suspend fun uploadPersonalDocuments(
//        @Part("personal") documentRequest: DocumentRequest,
        @Header("Authorization") token:String,
        @Part image: MultipartBody.Part,
//        jsonRequest: String
        @Part("reference_number")referenceNumber:RequestBody,
        @Part("issue_date")issueDate:RequestBody,
        @Part("expiry_date")expiryDate:RequestBody,
//        @Part("driver")driver:RequestBody,
        @Part("document_type")documentTYpe:RequestBody
    ): Response<DocumentResponse>

    @Multipart
    @POST("api/cargo_document/")
    suspend fun uploadCargoDocuments(
//        @Part("cargo") cargoDocsRequest: CargoDocsRequest,
        @Header("Authorization") token:String,
        @Part image: MultipartBody.Part,
//        jsonRequest: String
        @Part("reference_number")referenceNumber:RequestBody,
        @Part("issue_date")issueDate:RequestBody,
        @Part("expiry_date")expiryDate:RequestBody,
        @Part("cargo")cargo:RequestBody,
        @Part("cargo_tones")cargoTones:RequestBody,
//        @Part("driver")driver:RequestBody,
        @Part("document_type")documentTYpe:RequestBody
    ): Response<CargoDocsResponse>


    @Multipart
    @POST("api/truck-documents/")
    suspend fun uploadTruckDocuments(
        @Header("Authorization") token:String,
        @Part image: MultipartBody.Part,
//        jsonRequest: String
        @Part("reference_number")referenceNumber:RequestBody,
        @Part("issue_date")issueDate:RequestBody,
        @Part("expiry_date")expiryDate:RequestBody,
//        @Part("driver")driver:RequestBody,
        @Part("document_type")documentType:RequestBody
    ): Response<TruckDocsResponse>



//    @GET("api/personal-documents/")
//    fun getDocuments(): Response<List<SavedDocuments>>

//    @GET("api/personal-documents")
//    suspend fun getPersonalDocuments(): Response<List<SavedDocuments>>

//    @GET("api/truck-documents/")
//    fun getTruckDocuments():Response<List<TruckDocumentsData>>

//
//    @GET("api/cargo_document/")
//    fun getCargoDocuments(): Response<List<CargoDocsData>>
//
//    abstract fun uploadDocument(personalData: DocumentRequest): Response<DocumentResponse>

}






