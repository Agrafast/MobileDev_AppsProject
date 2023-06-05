package com.agrafast.util

import android.content.ContentResolver
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

val timeStamp: String = SimpleDateFormat(
  "dd-MMM-yyy",
  Locale("id")
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
  val file: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  return File.createTempFile(timeStamp, ".jpg", file)
}

class Helper {
  companion object{
    fun createFileFromUri(selectedImg: Uri, context: Context): File {
      val contentResolver: ContentResolver = context.contentResolver
      val myFile = createTempFile(context)

      val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
      val outputStream: OutputStream = FileOutputStream(myFile)
      val buf = ByteArray(1024)
      var len: Int
      while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
      outputStream.close()
      inputStream.close()
      return myFile
    }
  }
}
