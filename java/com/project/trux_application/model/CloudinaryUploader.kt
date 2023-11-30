package com.project.trux_application.model

import android.net.Uri
//import android.util.Log
//import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils

//class CloudinaryUploader {
//    companion object {
//        private val cloudinary = Cloudinary("cloudinary://${System.getenv("dkioamznc")}:${System.getenv("828226852764488")}:${System.getenv("3R84eig2OVfaaNFBRVMB4T4x_yo")}")
//        suspend fun upload(imageUri: Uri): String {
//            try {
//                Log.d("CloudinaryUploader", "ImageUri path: ${imageUri.path}")
//                val response = cloudinary.uploader().upload(imageUri.path, ObjectUtils.emptyMap())
//                return response["url"].toString()
//            } catch (e: Exception) {
//                Log.e("CloudinaryUploader", "Error uploading image to Cloudinary: $e")
//            }
//            return ""
//        }
//
import android.util.Log
import com.cloudinary.Cloudinary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun uploadImageToCloudinary(imagePath: String?): String {
    return withContext(Dispatchers.IO) {
        val cloudinary = Cloudinary("cloudinary://${System.getenv("dkioamznc")}:${System.getenv("828226852764488")}:${System.getenv("3R84eig2OVfaaNFBRVMB4T4x_yo")}")
        try {
            // Upload image to Cloudinary
            val result = cloudinary.uploader().upload(imagePath, null)
            val publicUrl = result["url"] as String
            Log.d("Cloudinary", "Image uploaded successfully. Public URL: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e("Cloudinary", "Error uploading image to Cloudinary: ${e.message}")
            ""
        }
    }
}