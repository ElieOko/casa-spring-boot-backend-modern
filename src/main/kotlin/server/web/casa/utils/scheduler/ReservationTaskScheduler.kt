package server.web.casa.utils.scheduler

import java.time.Instant
import java.util.*
import kotlin.jvm.java
import org.quartz.*
import org.springframework.stereotype.Service

@Service
class ReservationScheduler(private val scheduler: Scheduler) {

    fun scheduleOneShot(reservationId: Long, taskType: String, expireAt: Instant = Instant.now()) {
        val jobDetail = JobBuilder.newJob(ReservationJob::class.java)
            .withIdentity("reservation-$reservationId")
            .usingJobData("reservationId", reservationId.toString())
            .usingJobData("taskType", taskType)
            .storeDurably()
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity("trigger-$reservationId")
            .startAt(Date.from(Instant.now().plusSeconds(1 * 60)))
//            .startAt(Date.from(expireAt))
            .forJob(jobDetail)
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }
}