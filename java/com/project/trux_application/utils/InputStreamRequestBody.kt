package com.project.trux_application.utils

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.IOException
import okio.source
import java.io.InputStream

class InputStreamRequestBody(
    private val contentType: MediaType,
    private val contentResolver: ContentResolver,
    private val uri: Uri
) : RequestBody() {
    override fun contentType() = contentType

    override fun contentLength(): Long = -1

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri)
        Log.e("sink_wright",uri.toString())

        input?.use { sink.writeAll(it.source()) }
            ?: throw IOException("Could not open $uri")
    }
}