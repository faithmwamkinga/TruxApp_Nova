package com.project.trux_application.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


object Utility {
    // Function to save a Bitmap to a file
    fun saveBitmapToFile(bitmap: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/saved_images")
        myDir.mkdirs()

        val fname = "bitmap.png"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.path
    }
    fun generateFilename(prefix: String = "file", suffix: String = ".txt"): String {
        val timestamp = System.currentTimeMillis()
        return "$prefix$timestamp$suffix"
    }



}