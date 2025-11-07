package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.domain.model.request.ReservationRequest
import server.web.casa.app.user.application.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate
import kotlin.text.format

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
        val propertyEntity = propertyR.findById(request.propertyId).orElse(null)
        val userEntity = userR.findById(request.userId).orElse(null)
        val lastStatusReservationUserProperty = service.findByUserProperty(propertyEntity, userEntity)
        val statusLastReservationUserProperty = lastStatusReservationUserProperty?.last()?.status

        if (statusLastReservationUserProperty == ReservationStatus.PENDING){
            val responsePending = mapOf("error" to "You already have a pending reservation with this property")
            return ResponseEntity.ok().body(responsePending )
        }
        //if close or cancel we can verify the last before adding
        val lastReservationProperty = service.findByStartDateAndEndDateProperty(request.startDate, request.endDate, propertyEntity)

        val format = DateTimeFormatter.ofPattern("HH:mm:ss")

        val propertyBooked = lastReservationProperty
            ?.filter {
                val hour = LocalTime.parse(it.reservationHeure!!, format).plusHours(1)
                val timeUp = LocalTime.parse(request.reservationHeure, format)
                hour.isBefore(timeUp)
            }
            ?.sortedBy { it.reservationHeure }
        if(propertyBooked != null) {
            val responseHour = mapOf("error" to "Unfortunately, this time slot is already booked.")
            return ResponseEntity.ok().body(responseHour )
        }

        // check if property is available before adding
        if(!property.isAvailable){
            val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
            return ResponseEntity.ok().body(responseAvailable)
        }
        val reservationCreate = service.createReservation(dataReservation)
        val response = mapOf(
            "message" to "Votre reservation à la date du ${reservationCreate.startDate} au ${reservationCreate.endDate} a été créée avec succès",
            "user" to user,
            "property" to property,
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
     fun getReservationByProperty(@PathVariable propertyId: Long): ResponseEntity<out Map<String, Any?>> {
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

    @PutMapping("/cancel/{id}/{reason}")
     fun cancelReservation(
        @PathVariable id: Long,
        @RequestBody reason: String ?
    ): ResponseEntity<Map<String, Reservation?>> {
        val cancel = service.cancelOrKeepReservation(id, false,reason, ReservationStatus.CANCELLED)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/keep/{id}/{reason}")
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
        service.deleteReservationById(id)
        val response = mapOf("message" to "Reservation deleted successfully")
        return ResponseEntity.ok(response)
    }

}

