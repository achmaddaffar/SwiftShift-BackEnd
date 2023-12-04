package com.swiftshift.util

import java.io.File
import java.io.IOException

object FileUtil {

    fun returnFile(path: String, fileName: String): File? {
        try {
            val dir = File(path)
            var file: File? = null
            if (dir.exists()) {
                val files = dir.listFiles()
                file = files?.find { it.name == fileName && it.length() != 0L }
            }
            return file
        } catch (e: IOException) {
            println("IOException")
        } catch (e: Exception) {
            println("General Exception")
        }
        return null
    }
}