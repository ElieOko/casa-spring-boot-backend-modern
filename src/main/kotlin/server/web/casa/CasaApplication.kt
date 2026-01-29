package server.web.casa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import server.web.casa.security.monitoring.SentryService
import server.web.casa.utils.Mode
import server.web.casa.utils.storage.StorageProperties
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
@Profile(Mode.DEV)
class CasaApplication

fun main(args: Array<String>) {
	runApplication<CasaApplication>(*args)
}

@Controller
@Profile(Mode.DEV)
class HomeController(
    private val sentry: SentryService,
) {
    @GetMapping("/")
    fun home(request: HttpServletRequest):String {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            return  "index"
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.home.home.count",
                    distributionName = "api.home.home.latency"
                )
            )
        }
    }
}
