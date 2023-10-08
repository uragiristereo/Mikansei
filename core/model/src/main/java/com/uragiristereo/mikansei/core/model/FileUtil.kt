package com.uragiristereo.mikansei.core.model

import android.content.Context
import android.net.Uri
import timber.log.Timber
import java.io.File

object FileUtil {
    fun getTempDir(context: Context): File {
        return File(context.cacheDir, "temp")
            .also { dir ->
                if (!dir.isDirectory) {
                    dir.mkdir()
                }
            }
    }

    fun copyFile(
        context: Context,
        sourceUri: Uri,
        destinationUri: Uri,
    ) {
        val resolver = context.contentResolver

        resolver.openInputStream(sourceUri)?.use { inputStream ->
            resolver.openOutputStream(destinationUri)?.use { outputStream ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var read: Int

                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }

                Timber.d("file copied")
            }
        }
    }
}
