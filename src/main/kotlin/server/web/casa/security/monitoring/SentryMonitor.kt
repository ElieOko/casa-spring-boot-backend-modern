package server.web.casa.security.monitoring

import io.sentry.Sentry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct

@Component
class SentryInitializer(
    @Value("\${sentry.dsn}") private val dsn: String
) {
    @PostConstruct
    fun init() {
        Sentry.init { options ->
            options.dsn = dsn
        }
    }
}
