package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.property.application.service.*
import server.web.casa.app.reservation.application.service.ReservationHotelService
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationChambreHotelEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.reservation.ReservationHotelScope
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

class ReservationHotelController(
    private val service: ReservationHotelService,
    private val userS: UserService,
    private val chambrHTL: HotelChambreService,
    private val hotlS: HotelService,
    private val notif: NotificationReservationService,
    private val sentry: SentryService,
){
    @PostMapping("/{version}/${ReservationHotelScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ReservationChambreHotelRequest
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             val user = userS.findIdUser(request.userId)
             val chambrehotel = chambrHTL.getAll().find{ it.id == request.chambreId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "chambre hotel not found")).also { statusCode = it.statusCode.value().toString() }
             val hotel = hotlS.getAllHotel().find{ it.hotel.id == chambrehotel.hotelId }?.hotel ?: return ResponseEntity.badRequest().body(mapOf("error" to "hotel not found")).also { statusCode = it.statusCode.value().toString() }

             if(hotel.userId == user.userId){
                 val responseOwnProperty = mapOf("error" to "You can't reserve your own hotel")
                 return ResponseEntity.ok().body(responseOwnProperty ).also { statusCode = it.statusCode.value().toString() }
             }
             if (request.endDate < request.startDate || request.startDate < LocalDate.now()){
                 val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
                 return ResponseEntity.ok().body(responseNotFound ).also { statusCode = it.statusCode.value().toString() }
             }
             val dataReservation = ReservationChambreHotelEntity(
                 type = request.type.toString(),
                 isActive = true,
                 reservationHeure = request.reservationHeure,
                 userId = request.userId,
                 chambreId = chambrehotel.id!!,
                 message = request.message,
                 startDate = request.startDate,
                 endDate = request.endDate,
             )

            val lastStatusReservationUserProperty = service.findByUserProperty(chambrehotel.id, user.userId!!)
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

             val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, chambrehotel.id)

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
                 return ResponseEntity.ok().body(responseHour ).also { statusCode = it.statusCode.value().toString() }
             }

             // check if property is available before adding
             if(!hotel.isAvailable){
                 val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
                 return ResponseEntity.ok().body(responseAvailable).also { statusCode = it.statusCode.value().toString() }
             }
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
                 "proprietaire" to  userS.findIdUser( hotel.userId!!),
                "notificationSendState" to "notification bientot disponible"
             )
             return ResponseEntity.status(201).body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservationhotel.create.count",
                    distributionName = "api.reservationhotel.create.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(request: HttpServletRequest): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>>
    {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val data = service.findAllReservation()
            val response = mapOf("reservation" to data)
            return ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getallreservation.count",
                    distributionName = "api.reservationhotel.getallreservation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, ReservationChambreHotelDTO?>> {
         val startNanos = System.nanoTime()
        var statusCode = "200"
         try {
             val reservation = service.findById(id)
             val response = mapOf("reservation" to reservation)
             return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
         } finally {
             sentry.callToMetric(
                 MetricModel(
                     startNanos = startNanos,
                     status = statusCode,
                     route = "${request.method} /${request.requestURI}",
                     countName = "api.reservationhotel.getreservationbyid.count",
                     distributionName = "api.reservationhotel.getreservationbyid.latency"
                 )
             )
         }
     }
    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(request: HttpServletRequest, @PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findByStatus(status)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbystaus.count",
                    distributionName = "api.reservationhotel.getreservationbystaus.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(request: HttpServletRequest, @PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findByDate(inputDate)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbydate.count",
                    distributionName = "api.reservationhotel.getreservationbydate.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(request: HttpServletRequest, @PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findByMonth(month, year)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbymonthyear.count",
                    distributionName = "api.reservationhotel.getreservationbymonthyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(request: HttpServletRequest, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findByPYear(year)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbyyear.count",
                    distributionName = "api.reservationhotel.getreservationbyyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(request: HttpServletRequest, @PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findByInterval(startDateInput, endDateInput)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationininterval.count",
                    distributionName = "api.reservationhotel.getreservationininterval.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found")).also { statusCode = it.statusCode.value().toString() }
            //.orElseThrow{ RuntimeException("User not found with id: $userId") }
            val reservation = service.findByUser(user.userId!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbyuser.count",
                    distributionName = "api.reservationhotel.getreservationbyuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/chambre/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(request: HttpServletRequest, @PathVariable hotelId: Long): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val property = chambrHTL.getAll().find{ it.id == hotelId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found")).also { statusCode = it.statusCode.value().toString() }
            // chambrHTL.findById(hotelId) ?: return ResponseEntity.ok(mapOf("error" to "property not found"))//.orElse(null)
            val reservation = service.findByProperty(property.id!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.getreservationbyproperty.count",
                    distributionName = "api.reservationhotel.getreservationbyproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationHotelScope.PROTECTED}/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(request: HttpServletRequest, @PathVariable userId:Long): ResponseEntity<Map<String,  List<ReservationChambreHotelDTO>? >> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val user = userS.findIdUser(userId)
            val reservation = service.findByHostUser(user.userId!!)
            val response = mapOf("reservation" to reservation)
            return ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.findbyhostuser.count",
                    distributionName = "api.reservationhotel.findbyhostuser.latency"
                )
            )
        }
    }
    @PutMapping("/{version}/${ReservationHotelScope.PROTECTED}/update/status/{id}")
    suspend fun updateReservation(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val userRequest = userS.findIdUser(request.userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found")).also { statusCode = it.statusCode.value().toString() }
            val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("error" to "reservation not found")).also { statusCode = it.statusCode.value().toString() }

            val userId = reservation.userId
            val chambrehotel = chambrHTL.getAll().find{ it.id == reservation.chambreId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found")).also { statusCode = it.statusCode.value().toString() }
            val hotel = hotlS.getAllHotel().find{ it.hotel.id == chambrehotel.hotelId }?.hotel ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found")).also { statusCode = it.statusCode.value().toString() }
            val proprioId = hotel.userId
            // chambrHTL.findById( reservation.reservation.hotelId)

            val proprioCheck = userRequest.userId == proprioId
            val emetCheck = userRequest.userId == userId

            if(emetCheck || proprioCheck){
                if (proprioCheck){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    return ResponseEntity.ok(mapOf("reservation" to updated)).also { statusCode = it.statusCode.value().toString() }
                }

                if(request.status != ReservationStatus.APPROVED){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    return ResponseEntity.ok(mapOf("reservation" to updated)).also { statusCode = it.statusCode.value().toString() }
                }
            }
            return ResponseEntity.ok(mapOf("error" to "Authorization denied")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservationhotel.updatereservation.count",
                    distributionName = "api.reservationhotel.updatereservation.latency"
                )
            )
        }
    }

    @DeleteMapping("/{version}/${ReservationHotelScope.PROTECTED}/delete/{id}")
    suspend fun deleteReservation(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("message" to "Reservation not found")).also { statusCode = it.statusCode.value().toString() }
            val notificationDelete = notif.deleteByReservation(id)
            return if (notificationDelete) {
                service.deleteReservationById(id)
                ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully")).also { statusCode = it.statusCode.value().toString() }
            }else{
                return ResponseEntity.ok(mapOf("message" to "Something was wrong")).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.deletereservation.count",
                    distributionName = "api.reservationhotel.deletereservation.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${ReservationHotelScope.PROTECTED}/delete/all")
    suspend fun deleteReservationAll(request: HttpServletRequest): ResponseEntity<Map<String, String>> {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             val reservation = service.deleteAll()
            return ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservationhotel.deletereservationall.count",
                    distributionName = "api.reservationhotel.deletereservationall.latency"
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

        val propertyEntity = chambrHTL.findById(reservation.hotelId!!)
             propertyEntity!!.isAvailable = false
        chambrHTL.save(propertyEntity)*/
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
        val propertyEntity = chambrHTL.findById(reservation.hotelId!!)
           // .takeIf{ it.isNotEmpty() }!!
           // .filter { entity -> entity!!.hotelId==reservation.property.hotelId }[0] //.filter { }//findById(reservation.property.hotelId)
        propertyEntity!!.isAvailable = true
        chambrHTL.save(propertyEntity)*/
        val response = mapOf("DealCancel" to true, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }*/
}
