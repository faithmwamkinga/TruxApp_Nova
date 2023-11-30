package com.project.trux_application.model

import com.google.gson.annotations.SerializedName

data class TruckDocsResponse(
    @SerializedName("reference_number")val referenceNumber:String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("document_image") val documentImage: String,
    @SerializedName("document_type") val documentType: String,


)
