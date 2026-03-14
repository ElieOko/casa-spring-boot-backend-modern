package server.web.casa.utils.scheduler

import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReservationJob (private val reservationService: ReservationTaskService) : Job {
//    lateinit var reservationService: ReservationTaskService

    override fun execute(context: JobExecutionContext) {
        val reservationId = context.mergedJobDataMap.getLong("reservationId")
        val taskType = context.mergedJobDataMap.getString("taskType")

        // Appel de la logique métier
        reservationService.executeTask(reservationId, taskType)
    }
}