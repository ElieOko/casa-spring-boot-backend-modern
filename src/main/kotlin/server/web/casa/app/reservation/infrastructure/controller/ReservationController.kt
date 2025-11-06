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

import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate

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
        val dataReservation = Reservation(
            status = request.status,
            type = request.type,
            isActive = true,
            reservationHeure = request.reservationHeure,
            user = user,
            property = property!!,
            message = request.message,
            startDate = request.startDate,
            endDate = request.endDate,
        )
        val reservationCreate = service.createReservation(dataReservation)
        val response = mapOf(
            "message" to "Votre reservation à la date du ${reservationCreate.startDate} au ${reservationCreate.endDate} a été créée avec succès",
            "user" to user,
            "property" to property,
        )
        return ResponseEntity.status(201).body(response)
    }
    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
     fun getAllReserv(): ResponseEntity<Map<String, List<Reservation>>>
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
        val cancel = service.cancelOrKeepReservation(id, false,reason)
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/keep/{id}/{reason}")
     fun keepReservation(
        @PathVariable id: Long,
        @RequestBody reason: String ?
    ): ResponseEntity<Map<String, Reservation?>> {
        val keep = service.cancelOrKeepReservation(id, true, reason)
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

