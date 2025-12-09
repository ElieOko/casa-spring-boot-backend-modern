package server.web.casa.utils.storage

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("storage")
data class StorageProperties(
    var location: String = "/opt/backend/casa"
)

open class StorageException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)