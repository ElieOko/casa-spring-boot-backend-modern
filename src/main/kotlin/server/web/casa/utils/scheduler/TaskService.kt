package server.web.casa.utils.scheduler

import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import org.springframework.stereotype.Service

@Service
class ReservationTaskService(
   // private val reservationRepository: ReservationRepository,
   private val scheduler: ReservationScheduler
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun createReservation(taskId: Long, hoursUntilExecution: Long, taskType: String) {
        val expireAt = Instant.now().plusSeconds(hoursUntilExecution * 3600)
        scheduler.scheduleOneShot(taskId, taskType, expireAt)
    }

    fun executeTask(reservationId: Long, taskType: String) {
        // logique X ou Y
        println("Exécution tâche $taskType pour la réservation $reservationId")
        log.info("Exécution tâche $taskType pour la réservation $reservationId")
    }
}