package server.web.casa.utils.scheduler

import kotlinx.coroutines.runBlocking
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReservationJob (private val reservationService: ReservationTaskService) : Job {
//    lateinit var reservationService: ReservationTaskService

    override fun execute(context: JobExecutionContext) {
        val reservationId = context.mergedJobDataMap.getLong("reservationId")
        val minute = context.mergedJobDataMap.getLong("minute")
        val taskType = context.mergedJobDataMap.getString("taskType")
        val type = context.mergedJobDataMap.getString("type")
        runBlocking {
            // Appel de la logique métier
            reservationService.executeTask(reservationId, taskType, type)
        }


    }
}