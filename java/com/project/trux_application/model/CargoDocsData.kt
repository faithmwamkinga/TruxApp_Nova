package com.project.trux_application.model

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cargodocuments")
data class CargoDocsData(
    @PrimaryKey

    @SerializedName("reference_number")val referenceNumber:String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("document_image") val documentImage: String,
    @SerializedName("document_type") val documentType: String,
    @SerializedName("cargo_tones") val cargoTones: Double,
    val cargo:String,



)
