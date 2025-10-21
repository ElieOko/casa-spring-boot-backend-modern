package server.web.casa.utils.gcs

import lombok.Data
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Data
@Configuration
//@ConfigurationProperties(prefix = "gcs")
class BucketConfig(
    @Value("\${gcs.bucket-name}")
     var bucketName: String,
    @Value("\${gcs.subdirectory}")
     var subdirectory: String )