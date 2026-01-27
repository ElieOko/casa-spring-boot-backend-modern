package server.web.casa.security.monitoring

import io.sentry.Sentry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct

@Component
class SentryInitializer(
//    @Value("\${sentry.dsn}") private val dsn: String,
//    @Value("\${spring.profiles.active:dev}") private val env: String
) {
    @PostConstruct
    fun init() {
        Sentry.init { options ->
            options.dsn = "https://89ff13c7d1fa0e5092487c4875f6d42e@o4510707626082304.ingest.de.sentry.io/4510707628114000"
        }
    }
}
