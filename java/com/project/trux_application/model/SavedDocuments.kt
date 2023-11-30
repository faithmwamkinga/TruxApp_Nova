package com.project.trux_application.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.SkipQueryVerification
import com.google.gson.annotations.SerializedName


@Entity(tableName = "documents")
data class SavedDocuments (
    @PrimaryKey

    @SerializedName("reference_number")val referencenumber:String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("document_image") val documentImage: String,
    @SerializedName("document_type") val documentType: String,

    )

