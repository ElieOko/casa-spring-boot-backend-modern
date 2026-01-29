package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.reservation.domain.model.request.ReservationRequest
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.utils.Mode
import java.time.*
import java.time.format.DateTimeFormatter
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.notification.application.service.NotificationService
import server.web.casa.app.notification.domain.model.request.TagType
import server.web.casa.app.notification.infrastructure.persistence.entity.*
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import server.web.casa.route.reservation.ReservationScope
import server.web.casa.security.Auth
import server.web.casa.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import server.web.casa.security.monitoring.MetricModel

@Tag(name = "Reservation", description = "Reservation's Management")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)

class ReservationController(
    private val service: ReservationService,
    private val userS: UserService,
    private val propertyService: PropertyService,
    private val propertyR: PropertyRepository,
    private val userR: UserRepository,
    private val notif: NotificationReservationService,
    private val notificationService: NotificationService,
    private val notification2 : NotificationCasaRepository,
    private val auth: Auth,
    private val sentry: SentryService,
){
    private val log = LoggerFactory.getLogger(this::class.java)
    @PostMapping("/{version}/${ReservationScope.PRIVATE}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ReservationRequest
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             val user = userS.findIdUser(request.userId)
             val property = propertyService.findByIdProperty(request.propertyId)

             if(property.first.property.userId == user.userId){
                 val responseOwnProperty = mapOf("error" to "You can't reserve your own property")
                 ResponseEntity.ok().body(responseOwnProperty ).also { statusCode = it.statusCode.value().toString() }
             }
             if (request.endDate < request.startDate){
                 val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
                 ResponseEntity.ok().body(responseNotFound ).also { statusCode = it.statusCode.value().toString() }
             }
             val dataReservation = ReservationEntity(
                 status = request.status.toString(),
                 type = request.type.toString(),
                 isActive = true,
                 reservationHeure = request.reservationHeure,
                 userId = request.userId,
                 propertyId = property.first.property.propertyId!!,
                 message = request.message,
                 startDate = request.startDate,
                 endDate = request.endDate,
             )
             //!= verify
             val propertyEntity = propertyR.findById(request.propertyId)
                 //.orElseThrow { RuntimeException("Property not found") }

             val userEntity = userR.findById(request.userId)
                 //.orElseThrow { RuntimeException("User not found") }

            val lastStatusReservationUserProperty = service.findByUserProperty(propertyEntity?.id!!, userEntity?.userId!!)
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

             val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, propertyEntity.id)

             val propertyBooked = lastReservationProperty
                 ?.takeIf { it.isNotEmpty() }
                 ?.filter {
                     val start = LocalTime.parse(it.reservation?.reservationHeure!!, format)
                     val end = start.plusHours(1)
                     val newTimeR = LocalTime.parse(request.reservationHeure, format)
                     // newTimeR, verify interval
                     !newTimeR.isBefore(start) && newTimeR.isBefore(end)
                 }
                 ?.sortedBy { it.reservation?.reservationHeure }
             //if propertyBooked = null || empty we can add
             if(propertyBooked?.isNotEmpty() == true) {
                 val responseHour = mapOf(
                     "error" to "Unfortunately, this time slot is already booked.",
                     "data" to propertyBooked
                 )
                 ResponseEntity.ok().body(responseHour ).also { statusCode = it.statusCode.value().toString() }
             }

             // check if property is available before adding
             if(!property.first.property.isAvailable){
                 val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
                 ResponseEntity.ok().body(responseAvailable).also { statusCode = it.statusCode.value().toString() }
             }
             val reservationCreate = service.createReservation(dataReservation)
             val notification = notif.create(
                 NotificationReservation(
                     reservation = reservationCreate.reservation!!,
                     guestUser = userEntity,
                     hostUser = userR.findById( propertyEntity.user!!)!!
                 )
             )
             val note = notification2.save(
                 NotificationCasaEntity(
                     id = null,
                     userId = userR.findById( propertyEntity.user)!!.userId,
                     title = "Demande de visite reçue",
                     message = "Un client est intéressé par un bien et souhaite le visiter. Ne tardez pas à répondre \uD83D\uDE09",
                     tag = TagType.DEMANDES.toString(),
                 )
             )
             notificationService.sendNotificationToUser(userR.findById( propertyEntity.user)!!.userId.toString(),note.toDomain())

             val response = mapOf(
                 "message" to "Votre reservation à la date du ${reservationCreate.reservation.startDate} au ${reservationCreate.reservation.endDate} a été créée avec succès",
                 "reservation" to reservationCreate,
                 "user" to userEntity,
                 "proprietaire" to  userR.findById( propertyEntity.user),
                 //"property" to property,
                 "notificationSendState" to notification
             )
              ResponseEntity.status(201).body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservation.create.count",
                    distributionName = "api.reservation.create.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationScope.PROTECTED}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllReservation(request: HttpServletRequest): ResponseEntity<Map<String, List<ReservationDTO>>> = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             val checkAdmin = userS.isAdmin()
             if (!checkAdmin.first) throw ResponseStatusException(
                 HttpStatusCode.valueOf(404),
                 "Authorization denied."
             )
             val data = service.findAllReservation()
             val response = mapOf("reservation" to data)
            ResponseEntity.ok().body(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getallreservation.count",
                    distributionName = "api.reservation.getallreservation.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PRIVATE}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, ReservationDTO?>> = coroutineScope {
         val startNanos = System.nanoTime()
        var statusCode = "200"
         try {
             val checkAdmin = userS.isAdmin()
              val reservation = service.findById(id)
             if (!checkAdmin.first && reservation?.user?.userId != checkAdmin.second) throw ResponseStatusException(
                 HttpStatusCode.valueOf(404),
                 "Authorization denied."
             )
             val response = mapOf("reservation" to reservation)
             ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
         } finally {
             sentry.callToMetric(
                 MetricModel(
                     startNanos = startNanos,
                     status = statusCode,
                     route = "${request.method} /${request.requestURI}",
                     countName = "api.reservation.getreservationbyid.count",
                     distributionName = "api.reservation.getreservationbyid.latency"
                 )
             )
         }
     }
    @GetMapping("/{version}/${ReservationScope.PROTECTED}/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(request: HttpServletRequest, @PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationDTO>>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByStatus(status)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbystaus.count",
                    distributionName = "api.reservation.getreservationbystaus.latency"
                )
            )
        }
    }
    @GetMapping("/{version}/${ReservationScope.PROTECTED}/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(request: HttpServletRequest, @PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationDTO>>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByDate(inputDate)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbydate.count",
                    distributionName = "api.reservation.getreservationbydate.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(request: HttpServletRequest, @PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationDTO>>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByMonth(month, year)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbymonthyear.count",
                    distributionName = "api.reservation.getreservationbymonthyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(request: HttpServletRequest, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationDTO>>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByPYear(year)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbyyear.count",
                    distributionName = "api.reservation.getreservationbyyear.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(request: HttpServletRequest, @PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationDTO>?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByInterval(startDateInput, endDateInput)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationininterval.count",
                    distributionName = "api.reservation.getreservationininterval.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByUser(request: HttpServletRequest, @PathVariable userId: Long): ResponseEntity<out Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            val user : UserEntity = (userR.findById(userId) ?: ResponseEntity.ok(mapOf("error" to "user not found")).also { statusCode = it.statusCode.value().toString() }) as UserEntity
            if (!checkAdmin.first && checkAdmin.second != user.userId) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByUser(user.userId!!)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbyuser.count",
                    distributionName = "api.reservation.getreservationbyuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/property/{propertyId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(request: HttpServletRequest, @PathVariable propertyId: Long): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            val property : PropertyEntity? = (propertyR.findById(propertyId) ?: ResponseEntity.ok(mapOf("error" to "property not found")).also { statusCode = it.statusCode.value().toString() }) as PropertyEntity?
            if (!checkAdmin.first && checkAdmin.second != property?.user) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val reservation = service.findByProperty(property?.id!!)
            val response = mapOf("reservation" to reservation)
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.getreservationbyproperty.count",
                    distributionName = "api.reservation.getreservationbyproperty.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${ReservationScope.PROTECTED}/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(request: HttpServletRequest, @PathVariable userId:Long) = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
                    val userAuth = auth.user()
                    val isAdmin: Boolean? = userAuth?.second?.find{ true }
                    val user = userR.findById(userId) ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "user Not Found.")
                    log.info("***session:${userAuth?.first?.userId}")
                    log.info("***id-params:${user.userId}")
                    if (userId != userAuth?.first?.userId && isAdmin == false){
                        throw ResponseStatusException(
                            HttpStatusCode.valueOf(404),
                            "Authorization denied."
                        )
                    }
            //        val checkAdmin = userS.isAdmin()
            //        if (!checkAdmin.first || checkAdmin.second != user.userId) throw ResponseStatusException(
            //            HttpStatusCode.valueOf(404),
            //            "Authorization denied."
            //        )
                    val reservation = service.findByHostUser(user.userId!!)
                    val response = mapOf("reservation" to reservation)
                    ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.findbyhostuser.count",
                    distributionName = "api.reservation.findbyhostuser.latency"
                )
            )
        }
    }
    @PutMapping("/{version}/${ReservationScope.PROTECTED}/update/status/{id}")
    suspend fun updateReservation(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            val userRequest: UserEntity = (userR.findById(request.userId) ?:  ResponseEntity.ok(mapOf("error" to "user not found")).also { statusCode = it.statusCode.value().toString() }) as UserEntity
            val reservation: ReservationDTO = (service.findById(id) ?: ResponseEntity.ok(mapOf("error" to "reservation not found")).also { statusCode = it.statusCode.value().toString() }) as ReservationDTO
            val userId = reservation.reservation?.userId
            val proprioId = propertyR.findById( reservation.reservation?.propertyId!!)

            val proprioCheck = userRequest.userId == proprioId?.user
            val emetCheck = userRequest.userId == userId

            if(emetCheck || proprioCheck || checkAdmin.first){
                if (proprioCheck || checkAdmin.first){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    ResponseEntity.ok(mapOf("reservation" to updated)).also { statusCode = it.statusCode.value().toString() }
                }

                if(request.status != ReservationStatus.APPROVED){
                    val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                    ResponseEntity.ok(mapOf("reservation" to updated)).also { statusCode = it.statusCode.value().toString() }
                }
            }
            ResponseEntity.ok(mapOf("error" to "Authorization denied")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.reservation.updatereservation.count",
                    distributionName = "api.reservation.updatereservation.latency"
                )
            )
        }
    }

  /*  @PutMapping("/cancel/{id}")
    suspend fun cancelReservation(
        @PathVariable id: Long,
        @RequestBody reason: String?
    ): ResponseEntity<Map<String, ReservationEntity?>> {
        val cancel = service.cancelOrKeepReservation(id, false,reason, ReservationStatus.CANCELLED)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/keep/{id}/")
    suspend fun keepReservation(
        @PathVariable id: Long,
        @RequestBody reason: String ?
    ): ResponseEntity<Map<String, ReservationEntity?>> {
        val keep = service.cancelOrKeepReservation(id, true, reason, ReservationStatus.PENDING)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }*/

    @DeleteMapping("/{version}/${ReservationScope.PROTECTED}/delete/{id}")
    suspend fun deleteReservation(request: HttpServletRequest, @PathVariable id: Long): ResponseEntity<Map<String, String>>  = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val owner = service.findById(id)?.reservation ?: throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Reservation not found"
            )
            val propertyOwner = propertyService.findById(owner.propertyId!!)
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first && checkAdmin.second != owner.userId && checkAdmin.second != propertyOwner.user ) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val notificationDelete = notif.deleteByReservation(id)
            if (notificationDelete) {
                service.deleteReservationById(id)
                ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully")).also { statusCode = it.statusCode.value().toString() }
            }else{
                ResponseEntity.ok(mapOf("message" to "Something was wrong")).also { statusCode = it.statusCode.value().toString() }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.deletereservation.count",
                    distributionName = "api.reservation.deletereservation.latency"
                )
            )
        }
    }
    @DeleteMapping("/{version}/${ReservationScope.PROTECTED}/delete/all")
    suspend fun deleteReservationAll(request: HttpServletRequest): ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             val checkAdmin = userS.isAdmin()
             if (!checkAdmin.first) throw ResponseStatusException(
                 HttpStatusCode.valueOf(404),
                 "Authorization denied."
             )
             service.deleteAll()
            ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully")).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.deletereservationall.count",
                    distributionName = "api.reservation.deletereservationall.latency"
                )
            )
        }
    }

    @PutMapping("/{version}/${ReservationScope.PROTECTED}/notification/partners/{reservationId}")
    suspend fun dealConcludePartners(request: HttpServletRequest, @PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> = coroutineScope{
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val reservation = service.findById(reservationId)?.reservation
            if (reservation == null){
                val response = mapOf("error" to "reservation not found")
                ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
            }
            val propertyOwner = propertyService.findById(reservation?.propertyId!!)
            val checkAdmin = userS.isAdmin()
            if (!checkAdmin.first && checkAdmin.second != reservation.userId && checkAdmin.second != propertyOwner.user ) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val notification = notif.dealConcludedHost(reservation.id!!, true)
            val note = notification2.save(NotificationCasaEntity(id = null, userId = notification["host"].toString().toLong(), title = "Attribution confirmée", message = "Votre confirmation a bien été enregistrée. Le bien est attribué au client.", tag = TagType.DEMANDES.toString(),))
            notificationService.sendNotificationToUser(notification["host"].toString(),note.toDomain())
            val notificationGuest = notif.dealConcludedGuest(reservation.id , true)
            val note2 = notification2.save(NotificationCasaEntity(id = null, userId = notificationGuest["guest"].toString().toLong(), title = "Bonne nouvelle ", message = "Félicitations, votre demande a été validée et le bien vous a été accordé.", tag = TagType.DEMANDES.toString(),))
            notificationService.sendNotificationToUser(notificationGuest["guest"].toString(),note2.toDomain())
            val notificationState = notif.stateReservationHost(reservation.id, true)
            val propertyEntity = propertyR.findById(reservation.propertyId!!)
                 propertyEntity!!.isAvailable = false
            propertyR.save(propertyEntity)
            val response = mapOf(
                "DealConcludeHost" to notification["state"],
                "DealConcludeGuest" to notificationGuest["state"],
                "DealConcludeState" to notificationState,
                "message" to "True if it's successfully and null or false when unfulfilled")

            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.dealconcludepartners.count",
                    distributionName = "api.reservation.dealconcludepartners.latency"
                )
            )
        }
    }
   /* @PutMapping("/notification/guest/{reservationId}")
    fun dealconcluguest( @PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findId(reservationId)
        val notification = if(reservation != null) notif.dealConcludedGuest(reservation.reservationId, true) else null
        val response = mapOf("DealConcludeHost" to notification, "message" to "True if it's successfully and null or false when unfulfilled")
        return ResponseEntity.ok(response)
    }*/
    @PutMapping("/{version}/${ReservationScope.PROTECTED}/notification/state/{reservationId}/{state}")
    suspend fun stateReservationHost(
       request: HttpServletRequest,
       @PathVariable reservationId: Long,
       @PathVariable state: Boolean
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
             //val reservation = service.findById(reservationId)!!.reservation
            val checkAdmin = userS.isAdmin()
            val reservation = service.findById(reservationId)?.reservation
            if (reservation == null){
                val response = mapOf("error" to "reservation not found")
                ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
            }
            val propertyOwner = propertyService.findById(reservation?.propertyId!!)
            if (!checkAdmin.first && checkAdmin.second != reservation.userId && checkAdmin.second != propertyOwner.user ) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
             val notification = notif.stateReservationHost(reservation.id!!, state)
             val response = mapOf("DealConcludeHost" to notification, "message" to "True if it's successfully and null or false when unfulfilled")
             if (state){
                 val note2 = notification2.save(NotificationCasaEntity(id = null, userId = reservation.userId.toString().toLong(), title = "Visite confirmée", message = "Votre demande de visite a été approuvée. Préparez-vous pour le rendez-vous.", tag = TagType.DEMANDES.toString()))
                 notificationService.sendNotificationToUser(reservation.userId.toString(),note2.toDomain())
                 val hostId = propertyR.findById(reservation.propertyId)?.user
                 val note = notification2.save(NotificationCasaEntity(id = null, userId = hostId.toString().toLong(), title = "Rendez-vous validé", message = "La visite est confirmée. Tout est prêt pour accueillir le client.", tag = TagType.DEMANDES.toString()))
                 notificationService.sendNotificationToUser(hostId.toString(),note.toDomain())
             }
             ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.statereservationhost.count",
                    distributionName = "api.reservation.statereservationhost.latency"
                )
            )
        }
    }

    @PutMapping("/{version}/${ReservationScope.PROTECTED}/notification/cancel/{reservationId}")
    suspend fun dealCancel(request: HttpServletRequest, @PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        var statusCode = "200"
        try {
            val checkAdmin = userS.isAdmin()
            val reservation = service.findById(reservationId)?.reservation
            if (reservation == null){
                val response = mapOf("error" to "reservation not found")
                ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
            }
            val propertyOwner = propertyService.findById(reservation?.propertyId!!)
            if (!checkAdmin.first && checkAdmin.second != reservation.userId && checkAdmin.second != propertyOwner.user ) throw ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Authorization denied."
            )
            val notification = notif.stateReservationGuestCancel(reservation.id!!)
            val propertyEntity = propertyR.findById(reservation.propertyId)
               // .takeIf{ it.isNotEmpty() }!!
               // .filter { entity -> entity!!.propertyId==reservation.property.propertyId }[0] //.filter { }//findById(reservation.property.propertyId)
            propertyEntity!!.isAvailable = true
            propertyR.save(propertyEntity)
            val note = notification2.save(NotificationCasaEntity(id = null, userId = propertyEntity.user, title = "Annulation de visite", message = "Le client a annulé sa demande de visite. Aucune action n’est requise.", tag = TagType.DEMANDES.toString(),))
            notificationService.sendNotificationToUser(propertyEntity.user.toString(),note.toDomain())
            val response = mapOf("DealCancel" to notification, "message" to "Deal cancel successfully")
            ResponseEntity.ok(response).also { statusCode = it.statusCode.value().toString() }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = statusCode,
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.reservation.dealcancel.count",
                    distributionName = "api.reservation.dealcancel.latency"
                )
            )
        }
    }
}
class RequestUpdate(
    val status: ReservationStatus,
    val reason: String?,
    //val isActive: Boolean = true,
    val userId: Long
)
