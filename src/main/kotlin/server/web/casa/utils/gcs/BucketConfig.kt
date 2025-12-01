package server.web.casa.utils.gcs

import lombok.Data
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import server.web.casa.utils.Mode


@Data
@Configuration
@Profile(Mode.DEV)
//@ConfigurationProperties(prefix = "gcs")
class BucketConfig(
//    @Value("\${gcs.bucket-name}")
     var bucketName: String = "",
//    @Value("\${gcs.subdirectory}")
     var subdirectory: String = "")