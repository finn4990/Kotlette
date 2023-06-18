package com.Kotlette.ecommerce.file

import android.content.Context
import java.io.*
import java.lang.StringBuilder

class FileManager(private val context: Context) {

    fun writeToFile(fileName: String, data: String) {
        val outputStream : FileOutputStream
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readFromFile(fileName: String) : String {
        val inputStream: FileInputStream
        var readStream: InputStreamReader
        val buffer: BufferedReader
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null

        try {
            inputStream = context.openFileInput(fileName)
            readStream = InputStreamReader(inputStream)
            buffer = BufferedReader(readStream)
            while (run {
                    text = buffer.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }

            inputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()

    }
}