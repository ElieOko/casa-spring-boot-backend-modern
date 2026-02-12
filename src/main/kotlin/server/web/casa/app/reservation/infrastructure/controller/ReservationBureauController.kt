package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.property.application.service.BureauService
import server.web.casa.app.reservation.application.service.ReservationBureauService
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationBureauEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.reservation.ReservationBureauScope
import server.web.casa.utils.Mode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Reservation Bureau", description = "Reservation's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)

class ReservationBureauController(
    private val service: ReservationBureauService,
    private val userS: UserService,
    private val brxS: BureauService,
    private val notif: NotificationReservationService,
    private val sentry: SentryService,
){
    @PostMapping("/{version}/${ReservationBureauScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ReservationBureauRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
             val user = userS.findIdUser(request.userId)
             val bureau = brxS.findById(request.bureauId)

             if(bureau.userId == user.userId){
                 val responseOwnProperty = mapOf("error" to "You can't reserve your own property")
                 return ResponseEntity.ok().body(responseOwnProperty )}
             if (request.endDate < request.startDate){
                 val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
                 return ResponseEntity.ok().body(responseNotFound )}
             val dataReservation = ReservationBureauEntity(
                  status = request.status.toString(),
                 type = request.type.toString(),
                 isActive = true,
                 reservationHeure = request.reservationHeure,
                 userId = request.userId,
                 bureauId = bureau.id,
                 message = request.message,
                 startDate = request.startDate,
                 endDate = request.endDate,
             )
             //!= verify
             //val propertyEntity = brxS.findById(request.bureauId)
                 //.orElseThrow { RuntimeException("Property not found") }

             //val userEntity = userS.findIdUser(request.userId)
                 //.orElseThrow { RuntimeException("User not found") }

            val lastStatusReservationUserProperty = service.findByUserProperty(bureau.id!!, user.userId!!)
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

             val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, bureau.id!!)

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
             if(!bureau.isAvailable){
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
                 "message" to "Votre reservation à la date du ${reservationCreate.reservation?.startDate} au ${reservationCreate.reservation?.endDate} a été créée avec succès",
                 "reservation" to reservationCreate,
                 "proprietaire" to  userS.findIdUser( bureau.userId!!),
                 //"property" to property,
                // "notificationSendState" to notification
             )
             return ResponseEntity.status(201).body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservationbureau.create.count",
                    distributionName = "api.reservationbureau.create.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(request: HttpServletRequest): ResponseEntity<Map<String, List<ReservationBureauDTO>>>
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
                    countName = "api.reservationbureau.getallreservation.count",
                    distributionName = "api.reservationbureau.getallreservation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PRIVATE}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, ReservationBureauDTO?>> {
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
                     countName = "api.reservationbureau.getreservationbyid.count",
                     distributionName = "api.reservationbureau.getreservationbyid.latency"
                 )
             )
         }
     }
    @GetMapping("/{version}/${ReservationBureauScope.PRIVATE}/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(request: HttpServletRequest, @PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationBureauDTO>>> {
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
                    countName = "api.reservationbureau.getreservationbystaus.count",
                    distributionName = "api.reservationbureau.getreservationbystaus.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationBureauScope.PRIVATE}/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(request: HttpServletRequest, @PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationBureauDTO>>> {
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
                    countName = "api.reservationbureau.getreservationbydate.count",
                    distributionName = "api.reservationbureau.getreservationbydate.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PRIVATE}/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(request: HttpServletRequest, @PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationBureauDTO>>> {
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
                    countName = "api.reservationbureau.getreservationbymonthyear.count",
                    distributionName = "api.reservationbureau.getreservationbymonthyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(request: HttpServletRequest, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationBureauDTO>>> {
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
                    countName = "api.reservationbureau.getreservationbyyear.count",
                    distributionName = "api.reservationbureau.getreservationbyyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(request: HttpServletRequest, @PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationBureauDTO>?>> {
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
                    countName = "api.reservationbureau.getreservationininterval.count",
                    distributionName = "api.reservationbureau.getreservationininterval.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
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
                    countName = "api.reservationbureau.getreservationbyuser.count",
                    distributionName = "api.reservationbureau.getreservationbyuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}/bureau/{bureauId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(request: HttpServletRequest, @PathVariable bureauId: Long): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
                    val property = brxS.findById(bureauId) ?: return ResponseEntity.ok(mapOf("error" to "property not found"))//.orElse(null)
                    val reservation = service.findByProperty(property.id!!)
                    val response = mapOf("reservation" to reservation)
                    return ResponseEntity.ok(response)//        }?: RuntimeException("Property not found with id: $bureauId")
            //        val response = mapOf("message" to "Property not found with id: $bureauId")

            //        return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationbureau.getreservationbyproperty.count",
                    distributionName = "api.reservationbureau.getreservationbyproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationBureauScope.PROTECTED}/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(request: HttpServletRequest, @PathVariable userId:Long): ResponseEntity<Map<String,  List<ReservationBureauDTO>? >> {
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
                    countName = "api.reservationbureau.findbyhostuser.count",
                    distributionName = "api.reservationbureau.findbyhostuser.latency"
                )
            )
        }
    }
    @PutMapping("/{version}/${ReservationBureauScope.PROTECTED}/update/status/{id}")
    suspend fun updateReservation(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val userRequest = userS.findIdUser(request.userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
            val reservation = service.findById(id) ?: return ResponseEntity.ok(mapOf("error" to "reservation not found"))
            val userId = reservation.reservation.userId
            val proprioId = brxS.findById( reservation.reservation.bureauId!!)

            val proprioCheck = userRequest.userId == proprioId.userId
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
                    countName = "api.reservationbureau.updatereservation.count",
                    distributionName = "api.reservationbureau.updatereservation.latency"
                )
            )
        }
    }

  /*  @PutMapping("/cancel/{id}")
    suspend fun cancelReservation(
        @PathVariable id: Long,
        @RequestBody reason: String?
    ): ResponseEntity<Map<String, ReservationBureauEntity?>> {
        val cancel = service.cancelOrKeepReservation(id, false,reason, ReservationStatus.CANCELLED)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/keep/{id}/")
    suspend fun keepReservation(
        @PathVariable id: Long,
        @RequestBody reason: String ?
    ): ResponseEntity<Map<String, ReservationBureauEntity?>> {
        val keep = service.cancelOrKeepReservation(id, true, reason, ReservationStatus.PENDING)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{version}/${ReservationBureauScope.PROTECTED}/delete/{id}")
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
                    countName = "api.reservationbureau.deletereservation.count",
                    distributionName = "api.reservationbureau.deletereservation.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${ReservationBureauScope.PROTECTED}/delete/all")
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
                    countName = "api.reservationbureau.deletereservationall.count",
                    distributionName = "api.reservationbureau.deletereservationall.latency"
                )
            )
        }
    }

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

        val propertyEntity = brxS.findById(reservation.bureauId!!)
             propertyEntity!!.isAvailable = false
        brxS.save(propertyEntity)*/
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
        val propertyEntity = brxS.findById(reservation.bureauId!!)
           // .takeIf{ it.isNotEmpty() }!!
           // .filter { entity -> entity!!.bureauId==reservation.property.bureauId }[0] //.filter { }//findById(reservation.property.bureauId)
        propertyEntity!!.isAvailable = true
        brxS.save(propertyEntity)*/
        val response = mapOf("DealCancel" to true, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }*/
}
