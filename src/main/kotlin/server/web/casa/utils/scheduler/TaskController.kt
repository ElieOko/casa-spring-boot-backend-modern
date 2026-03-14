package server.web.casa.utils.scheduler

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.route.account.SchedulerScope
import server.web.casa.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class TaskController(
    private val task : ReservationScheduler,
    private val sentry: SentryService,
) {
    @Operation(summary = "Scheduler task")
    @GetMapping("/{version}/${SchedulerScope.PUBLIC}/{id}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getScheduler(request: HttpServletRequest,@PathVariable("id") id : Long) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            task.scheduleOneShot(id,"test execution")
            ResponseEntity.status(201).body(mapOf("task" to "tache executer avec succes"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.typecard.getalltypecard.count",
                    distributionName = "api.typecard.getalltypecard.latency"
                )
            )
        }
    }
}