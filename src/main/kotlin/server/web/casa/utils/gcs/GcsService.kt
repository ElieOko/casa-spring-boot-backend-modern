package server.web.casa.utils.gcs

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.*
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class GcsService(
) {
    private var storage: Storage? = null
    private var bucketConfig: BucketConfig? = null
    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        val credentialsStream = ClassPathResource("casa-store.json").inputStream
        val credentials = GoogleCredentials.fromStream(credentialsStream)
        storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .service
    }
    @Throws(IOException::class)
    fun uploadFile(file: MultipartFile,directory : String = ""): String? {
        log.info("Gcs service****file:${file.name}")
        val fileName = "casa/${directory}${file.originalFilename}"
        log.info("Gcs service****fileName:${fileName}")
        val blob = storage?.create(
            BlobInfo.newBuilder("staging.infinite-strata-226508.appspot.com", fileName).build(),
            file.bytes
        )
        log.info("Gcs service****blod:${blob?.mediaLink}")
        return blob?.mediaLink
    }

    fun deleteFile(fileName: String?) {
        val blobId = BlobId.of(bucketConfig?.bucketName, bucketConfig?.subdirectory + "/" + fileName)
        storage?.delete(blobId)
    }

    fun getFile(fileName: String?): Blob? {
        return storage?.get(bucketConfig?.bucketName, fileName)
    }
}