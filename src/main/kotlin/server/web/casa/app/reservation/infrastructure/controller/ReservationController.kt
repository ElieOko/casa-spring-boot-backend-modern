package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.request.ReservationRequest
import server.web.casa.app.reservation.infrastructure.persistence.mapper.ReservationMapper
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val ROUTE_RESERVATION = ReservationRoute.RESERVATION

@Tag(name = "Reservation", description = "Reservation's Management")
@RestController
@RequestMapping(ROUTE_RESERVATION)
@Profile(Mode.DEV)

class ReservationController(
    private val service: ReservationService,
    private val userService: UserService,
    private val propertyService: PropertyService,
    private val propertyR: PropertyRepository,
    private val userR: UserRepository,
    private val notif: NotificationReservationService,
    private val mapperR: ReservationMapper
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: ReservationRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userService.findIdUser(request.userId)
        val property = propertyService.findByIdProperty(request.propertyId)

        if (user == null || property == null){
            val responseNotFound = mapOf("error" to "User or property not found")
            return ResponseEntity.ok().body(responseNotFound )
        }

        if(property.user == user){
            val responsePending = mapOf("error" to "You can't reserve your own property")
            return ResponseEntity.ok().body(responsePending )
        }
        if (request.endDate < request.startDate){
            val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
            return ResponseEntity.ok().body(responseNotFound )
        }
        val dataReservation = Reservation(
            status = request.status,
            type = request.type,
            isActive = true,
            reservationHeure = request.reservationHeure,
            user = user,
            property = property,
            message = request.message,
            startDate = request.startDate,
            endDate = request.endDate,
        )
        //!= verify
        val propertyEntity = propertyR.findById(request.propertyId)
            .orElseThrow { RuntimeException("Property not found") }

        val userEntity = userR.findById(request.userId)
            .orElseThrow { RuntimeException("User not found") }

        val lastStatusReservationUserProperty = service.findByUserProperty(propertyEntity, userEntity)
        val statusLastReservationUserProperty = lastStatusReservationUserProperty
                                                ?.takeIf { it.isNotEmpty() }
                                                ?.last()
                                                ?.status
        if (statusLastReservationUserProperty == ReservationStatus.PENDING){
            val responsePending = mapOf("error" to "You already have a pending reservation with this property")
            return ResponseEntity.ok().body(responsePending )
        }
        //if close or cancel we can verify the last before adding
        val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, propertyEntity)

        val format = DateTimeFormatter.ofPattern("HH:mm:ss")

        val propertyBooked = lastReservationProperty
            ?.takeIf { it.isNotEmpty() }
            ?.filter {
                val start = LocalTime.parse(it.reservationHeure!!, format)
                val end = start.plusHours(1)
                val newTimeR = LocalTime.parse(request.reservationHeure, format)
                // newTimeR, verify interval
                !newTimeR.isBefore(start) && newTimeR.isBefore(end)
            }
            ?.sortedBy { it.reservationHeure }
        //if propertyBooked = null || empty we can add
        if(propertyBooked?.isNotEmpty() == true) {
            val responseHour = mapOf(
                "error" to "Unfortunately, this time slot is already booked.",
                "data" to propertyBooked
            )
            return ResponseEntity.ok().body(responseHour )
        }

        // check if property is available before adding
        if(!property.isAvailable){
            val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
            return ResponseEntity.ok().body(responseAvailable)
        }
        val reservationCreate = service.createReservation(dataReservation)
        val notification = notif.create(
            NotificationReservation(
                reservation = mapperR.toEntity(reservationCreate),
                guestUser = userEntity,
                hostUser = propertyEntity.user!!
            )
        )
        val response = mapOf(
            "message" to "Votre reservation à la date du ${reservationCreate.startDate} au ${reservationCreate.endDate} a été créée avec succès",
            "reservation" to reservationCreate,
            "user" to user,
            "property" to property,
            "notificationSendState" to notification
        )
        return ResponseEntity.status(201).body(response)
    }
    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getAllReservation(): ResponseEntity<Map<String, List<Reservation>>>
    {
        val data = service.findAllReservation()
        val response = mapOf("reservation" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, Reservation?>> {
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByStaus(@PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<Reservation>>> {
        val reservation = service.findByStatus(status)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByDate(@PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<Reservation>>> {
        val reservation = service.findByDate(inputDate)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByMonthYear(@PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<Reservation>>> {
        val reservation = service.findByMonth(month, year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByYear(@PathVariable year: Int): ResponseEntity<Map<String, List<Reservation>>> {
        val reservation = service.findByPYear(year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationInInterval(@PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<Reservation>?>> {
        val reservation = service.findByInterval(startDateInput, endDateInput)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByUser(@PathVariable userId: Long): ResponseEntity<Map<String, List<Reservation>?>> {

        val user = userR.findById(userId).orElseThrow{
                 RuntimeException("User not found with id: $userId")
        }
        val reservation = service.findByUser(user)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/property/{propertyId}", produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getReservationByProperty(@PathVariable propertyId: Long): ResponseEntity<Map<String, Any?>> {
        val property = propertyR.findById(propertyId).orElse(null)
        val reservation = service.findByProperty(property)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
//        }?: RuntimeException("Property not found with id: $propertyId")
//        val response = mapOf("message" to "Property not found with id: $propertyId")

//        return ResponseEntity.badRequest().body(response)
    }
    @PutMapping("/update/status/{id}")
     fun updateReservation(
        @PathVariable id: Long,
        @RequestBody status: ReservationStatus
    ): ResponseEntity<Map<String, Reservation?>> {
        val updated = service.updateStatusById(id, status)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/cancel/{id}")
     fun cancelReservation(
        @PathVariable id: Long,
        @RequestBody reason: String?
    ): ResponseEntity<Map<String, Reservation?>> {
        val cancel = service.cancelOrKeepReservation(id, false,reason, ReservationStatus.CANCELLED)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/keep/{id}/")
     fun keepReservation(
        @PathVariable id: Long,
        @RequestBody reason: String ?
    ): ResponseEntity<Map<String, Reservation?>> {
        val keep = service.cancelOrKeepReservation(id, true, reason, ReservationStatus.PENDING)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/delete/{id}")
     fun deleteReservation(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val reservation = service.findId(id) ?: return ResponseEntity.ok(mapOf("message" to "Reservation not found"))
        val notificationDelete = notif.deleteByReservation(id)
        return if (notificationDelete) {
            service.deleteReservationById(id)
            ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully"))
        }else{
            return ResponseEntity.ok(mapOf("message" to "Something was wrong"))
        }
    }
    @PutMapping("/notification/partners/{reservationId}")
    fun dealConcludePartners(@PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findId(reservationId)
        if (reservation ==null){
            val response = mapOf("error" to "reservation not found")
            return ResponseEntity.ok(response)
        }
        val notification = notif.dealConcludedHost(reservation.reservationId, true)
        val notificationGuest = notif.dealConcludedGuest(reservation.reservationId, true)
        val notificationState = notif.stateReservationHost(reservation.reservationId, true)

        val propertyEntity = propertyR.findAll()
            .takeIf{ it.isNotEmpty() }!!
            .filter { entity -> entity!!.propertyId==reservation.property.propertyId }[0] //.filter { }//findById(reservation.property.propertyId)
        propertyEntity!!.isAvailable = false
        propertyR.save(propertyEntity)
        val response = mapOf(
            "DealConcludeHost" to notification,
            "DealConcludeGuest" to notificationGuest,
            "DealConcludeState" to notificationState,
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
    fun stateReservationHost(
       @PathVariable reservationId: Long,
       @PathVariable state: Boolean
    ): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findId(reservationId)
        val notification = if(reservation != null) notif.stateReservationHost(reservation.reservationId, state) else null
        val response = mapOf("DealConcludeHost" to notification, "message" to "True if it's successfully and null or false when unfulfilled")
        return ResponseEntity.ok(response)
    }
    @PutMapping("/notification/cancel/{reservationId}")
    fun dealCancel( @PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findId(reservationId)
        if (reservation ==null){
            val response = mapOf("error" to "reservation not found")
            return ResponseEntity.ok(response)
        }
        val notification = notif.stateReservationGuestCancel(reservation.reservationId)
        val propertyEntity = propertyR.findAll()
            .takeIf{ it.isNotEmpty() }!!
            .filter { entity -> entity!!.propertyId==reservation.property.propertyId }[0] //.filter { }//findById(reservation.property.propertyId)
        propertyEntity!!.isAvailable = true
        propertyR.save(propertyEntity)
        val response = mapOf("DealCancel" to notification, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }
}

