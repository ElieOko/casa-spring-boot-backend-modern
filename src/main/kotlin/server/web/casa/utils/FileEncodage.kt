package server.web.casa.utils

import org.springframework.mock.web.MockMultipartFile
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.util.Base64

fun convertFile(base64 : String): File {
    val decodedBytes = Base64.getDecoder().decode(base64)
    val filePath = "image_${LocalDateTime.now().second}_.png"
    val outputFile = File(filePath)
    outputFile.writeBytes(decodedBytes)
    return outputFile
}

fun base64ToMultipartFile(
    base64: String,
    prefix : String ="",
    fieldName: String = "file",
    mimeType: String = "image/png"
): MockMultipartFile {
    val timestamp = System.currentTimeMillis()
    val fileName: String = "image_${prefix}_${timestamp}.png"
    val imageBytes = Base64.getDecoder().decode(base64)
    return MockMultipartFile(fieldName, fileName, mimeType, imageBytes)
}