package com.project.trux_application.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Part

data class DocumentRequest(

    @SerializedName("reference_number")val referencenumber:String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("document_type") val documentType: String,
    val token :String,



)

