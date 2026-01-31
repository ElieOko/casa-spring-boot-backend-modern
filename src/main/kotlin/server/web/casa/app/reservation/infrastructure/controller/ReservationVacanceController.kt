package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.property.application.service.VacanceService
import server.web.casa.app.reservation.application.service.ReservationVacanceService
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationVacanceEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.reservation.ReservationVacanceScope
import server.web.casa.utils.Mode
import java.time.*
import java.time.format.DateTimeFormatter
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Reservation", description = "Reservation's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)

class ReservationVacanceController(
    private val service: ReservationVacanceService,
    private val userS: UserService,
    private val vacS: VacanceService,
    private val notif: NotificationReservationService,
    private val sentry: SentryService,
){
    @PostMapping("/{version}/${ReservationVacanceScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ReservationVacanceRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
             val user = userS.findIdUser(request.userId)
             val vacance = vacS.getAllVacance().find{ it.id == request.vacanceId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hotel not found"))
            if(vacance.userId == user.userId){
                 val responseOwnProperty = mapOf("error" to "You can't reserve your own vacance")
                 return ResponseEntity.ok().body(responseOwnProperty )}
             if (request.endDate < request.startDate || request.startDate < LocalDate.now()){
                 val responseErrorDate = mapOf("error" to "End date must be after or equal to start date")
                 return ResponseEntity.ok().body(responseErrorDate )}
             val owner = userS.findIdUser( vacance.userId!!)
             val dataReservation = ReservationVacanceEntity(
                 type = request.type.toString(),
                 isActive = true,
                 reservationHeure = request.reservationHeure,
                 userId = request.userId,
                 vacanceId = vacance.id!!,
                 message = request.message,
                 startDate = request.startDate,
                 endDate = request.endDate,
             )

            val lastStatusReservationUserProperty = service.findByUserProperty(vacance.id, user.userId!!)
                                                     ?.lastOrNull()?.reservation
             val format = DateTimeFormatter.ofPattern("HH:mm:ss")

             if (lastStatusReservationUserProperty != null){
                 val status = lastStatusReservationUserProperty.status
                 val reservationHeure = lastStatusReservationUserProperty.reservationHeure
                 val reservationId = lastStatusReservationUserProperty.id
                 val timeNow = LocalTime.now()
                 val startInterval = LocalTime.parse(reservationHeure!!, format)
                 val endInterval = startInterval.plusHours(1)
                 val timeRequest = LocalTime.parse(request.reservationHeure, format)

                 if(status == ReservationStatus.PENDING.toString() && timeNow.isAfter(endInterval))
                 {
                     //!timeRequest.isBefore(startInterval) && timeRequest.isBefore(endInterval
                     val updated = service.updateStatusById(reservationId!!, ReservationStatus.CANCELLED)
                 }
             }

             val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, vacance.id)

             val propertyBooked = lastReservationProperty
                 ?.takeIf { it.isNotEmpty() }
                 ?.filter {
                     val start = LocalTime.parse(it.reservation.reservationHeure!!, format)
                     val end = start.plusHours(1)
                     val newTimeR = LocalTime.parse(request.reservationHeure, format)
                     // newTimeR, verify interval
                     !newTimeR.isBefore(start) && newTimeR.isBefore(end)
                 }
                 ?.sortedBy { it.reservation.reservationHeure }
             //if propertyBooked = null || empty we can add
             if(propertyBooked?.isNotEmpty() == true) {
                 val responseHour = mapOf(
                     "error" to "Unfortunately, this time slot is already booked.",
                     "data" to propertyBooked
                 )
                 return ResponseEntity.ok().body(responseHour )}

             // check if property is available before adding
             if(!vacance.isAvailable){
                 val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
                 return ResponseEntity.ok().body(responseAvailable)}
                 val reservationCreate = service.createReservation(dataReservation)
             /* val notification = notif.create(
                  NotificationReservation(
                      reservation = reservationCreate.reservation!!,
                      guestUser = userEntity,
                      hostUser = userR.findById( propertyEntity.user!!)!!
                  )
              )*/
             val response = mapOf(
                 "message" to "Votre reservation à la date du ${reservationCreate.reservation.startDate} au ${reservationCreate.reservation?.endDate} a été créée avec succès",
                 "reservation" to reservationCreate,
                 "proprietaire" to  owner,
                 "notificationSendState" to "bientot disponible"
             )
             return ResponseEntity.status(201).body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservationvacance.create.count",
                    distributionName = "api.reservationvacance.create.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(request: HttpServletRequest): ResponseEntity<Map<String, List<ReservationVacanceDTO>>>
    {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllReservation()
            val response = mapOf("reservation" to data)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getallreservation.count",
                    distributionName = "api.reservationvacance.getallreservation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, ReservationVacanceDTO?>> {
         val startNanos = System.nanoTime()
         try {
             val reservation = service.findById(id)
             val response = mapOf("reservation" to reservation)
             return ResponseEntity.ok(response)
        } finally {
             sentry.callToMetric(
                 MetricModel(
                     startNanos = startNanos,
                     status = "200",
                     route = "${request.method} /${request.requestURI}",
                     countName = "api.reservationvacance.getreservationbyid.count",
                     distributionName = "api.reservationvacance.getreservationbyid.latency"
                 )
             )
         }
     }
    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(request: HttpServletRequest, @PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationVacanceDTO>>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findByStatus(status)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbystaus.count",
                    distributionName = "api.reservationvacance.getreservationbystaus.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(request: HttpServletRequest, @PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationVacanceDTO>>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findByDate(inputDate)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbydate.count",
                    distributionName = "api.reservationvacance.getreservationbydate.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(request: HttpServletRequest, @PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationVacanceDTO>>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findByMonth(month, year)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbymonthyear.count",
                    distributionName = "api.reservationvacance.getreservationbymonthyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(request: HttpServletRequest, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationVacanceDTO>>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findByPYear(year)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbyyear.count",
                    distributionName = "api.reservationvacance.getreservationbyyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(request: HttpServletRequest, @PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationVacanceDTO>?>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findByInterval(startDateInput, endDateInput)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationininterval.count",
                    distributionName = "api.reservationvacance.getreservationininterval.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val user = userS.findIdUser(userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))//.orElseThrow{ RuntimeException("User not found with id: $userId") }
            val reservation = service.findByUser(user.userId!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbyuser.count",
                    distributionName = "api.reservationvacance.getreservationbyuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/vacance/{vacanceId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(request: HttpServletRequest, @PathVariable vacanceId: Long): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val property =  vacS.getAllVacance().find{ it.id == vacanceId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "vacance not found"))
            val reservation = service.findByProperty(property.id!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.getreservationbyproperty.count",
                    distributionName = "api.reservationvacance.getreservationbyproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationVacanceScope.PROTECTED}/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(request: HttpServletRequest, @PathVariable userId:Long): ResponseEntity<Map<String,  List<ReservationVacanceDTO>? >> {
        val startNanos = System.nanoTime()
        try {
            val user = userS.findIdUser(userId)
            val reservation = service.findByHostUser(user.userId!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.findbyhostuser.count",
                    distributionName = "api.reservationvacance.findbyhostuser.latency"
                )
            )
        }
    }
    @PutMapping("/{version}/${ReservationVacanceScope.PROTECTED}/update/status/{id}")
    suspend fun updateReservation(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val userRequest = userS.findIdUser(request.userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
            val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("error" to "reservation not found"))
            val userId = reservation.userId
            val proprioId = vacS.getAllVacance().find{it.id == reservation.vacanceId}?.userId ?: return ResponseEntity.badRequest().body(mapOf("error" to "vacance not found"))
            val proprioCheck = userRequest.userId == proprioId
            val emetCheck = userRequest.userId == userId

            if(emetCheck || proprioCheck){
                if (proprioCheck){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    return ResponseEntity.ok(mapOf("reservation" to updated))}

                if(request.status != ReservationStatus.APPROVED){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    return ResponseEntity.ok(mapOf("reservation" to updated))}
            }
            return ResponseEntity.ok(mapOf("error" to "Authorization denied"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservationvacance.updatereservation.count",
                    distributionName = "api.reservationvacance.updatereservation.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${ReservationVacanceScope.PROTECTED}/delete/{id}")
    suspend fun deleteReservation(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
            val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("message" to "Reservation not found"))
            val notificationDelete = notif.deleteByReservation(id)
            return if (notificationDelete) {
                service.deleteReservationById(id)
                ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully"))}else{
                return ResponseEntity.ok(mapOf("message" to "Something was wrong"))}
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.deletereservation.count",
                    distributionName = "api.reservationvacance.deletereservation.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${ReservationVacanceScope.PROTECTED}/delete/all")
    suspend fun deleteReservationAll(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        try {
             val reservation = service.deleteAll()
            return ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationvacance.deletereservationall.count",
                    distributionName = "api.reservationvacance.deletereservationall.latency"
                )
            )
        }
    }
/*
    @PutMapping("/notification/partners/{reservationId}")
    suspend fun dealConcludePartners(@PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findById(reservationId)?.reservation
        if (reservation == null){
            val response = mapOf("error" to "reservation not found")
            return ResponseEntity.ok(response)
        }
        /*
        val notification = notif.dealConcludedHost(reservation.id!!, true)
        val notificationGuest = notif.dealConcludedGuest(reservation.id , true)
        val notificationState = notif.stateReservationHost(reservation.id, true)

        val propertyEntity = vacS.findById(reservation.vacanceId!!)
             propertyEntity!!.isAvailable = false
        vacS.save(propertyEntity)*/
        val response = mapOf(
            "DealConcludeHost" to true,
            "DealConcludeGuest" to true,
            "DealConcludeState" to true,
            "message" to "True if it's successfully and null or false when unfulfilled")

        return ResponseEntity.ok(response)
    }
   /* @PutMapping("/notification/guest/{reservationId}")
    fun dealconcluguest( @PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findId(reservationId)
        val notification = if(reservation != null) notif.dealConcludedGuest(reservation.reservationId, true) else null
        val response = mapOf("DealConcludeHost" to notification, "message" to "True if it's successfully and null or false when unfulfilled")
        return ResponseEntity.ok(response)
    }*/
    @PutMapping("/notification/state/{reservationId}/{state}")
    suspend fun stateReservationHost(
       @PathVariable reservationId: Long,
       @PathVariable state: Boolean
    ): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findById(reservationId)!!.reservation
        val notification = if(reservation != null) notif.stateReservationHost(reservation.id!!, state) else null
        val response = mapOf("DealConcludeHost" to notification, "message" to "True if it's successfully and null or false when unfulfilled")
        return ResponseEntity.ok(response)
    }
    @PutMapping("/notification/cancel/{reservationId}")
    suspend fun dealCancel(@PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findById(reservationId)!!.reservation
        if (reservation ==null){
            val response = mapOf("error" to "reservation not found")
            return ResponseEntity.ok(response)
        }/*
        val notification = notif.stateReservationGuestCancel(reservation.id!!)
        val propertyEntity = vacS.findById(reservation.vacanceId!!)
           // .takeIf{ it.isNotEmpty() }!!
           // .filter { entity -> entity!!.vacanceId==reservation.property.vacanceId }[0] //.filter { }//findById(reservation.property.vacanceId)
        propertyEntity!!.isAvailable = true
        vacS.save(propertyEntity)*/
        val response = mapOf("DealCancel" to true, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }*/
}
