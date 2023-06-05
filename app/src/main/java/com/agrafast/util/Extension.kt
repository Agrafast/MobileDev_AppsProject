package com.agrafast.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val timeStamp: String = SimpleDateFormat("dd-MMM-yyy", Locale("id")).format(Date())

fun Context.createTempFile(): File {
  return File.createTempFile(timeStamp, ".jpg", externalCacheDir)
}


fun Context.createFileFromUri(selectedImg: Uri): File {
  val contentResolver: ContentResolver = this.contentResolver
  val myFile = this.createTempFile()

  val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
  val outputStream: OutputStream = FileOutputStream(myFile)
  val buf = ByteArray(1024)
  var len: Int
  while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
  outputStream.close()
  inputStream.close()
  return myFile
}

fun File.reduceFileImage(max : Int = 1000000): File {
  val bitmap = BitmapFactory.decodeFile(this.path)
  var compressQuality = 100
  var streamLength: Int
  do {
    val bmpStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()
    streamLength = bmpPicByteArray.size
    compressQuality -= 5
  } while (streamLength > max)
  bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
  return this
}