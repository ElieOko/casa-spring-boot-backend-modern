package server.web.casa.app.reservation.infrastructure.controller

import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import server.web.casa.app.actor.domain.model.Bailleur
import server.web.casa.app.property.application.service.PropertyService
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.reservation.domain.model.Reservation
import server.web.casa.app.reservation.domain.model.request.ReservationRequest
import server.web.casa.app.user.application.UserService

import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode

const val ROUTE_RESERVATION = ReservationRoute.RESERVATION

@RestController
@RequestMapping(ROUTE_RESERVATION)
@Profile(Mode.DEV)

class ReservationController(
    private val service: ReservationService,
    private val userService: UserService,
    private val propertyService: PropertyService
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @Valid @RequestBody request: ReservationRequest
    ): ResponseEntity<Map<String, Any?>> {
        //val user = userService.findIdUser (request.user.userId)
        val user = request.user?.userId?.let { userService.findIdUser(it) }
        val property = propertyService.findByIdProperty(request.property.propertyId)
        val dataReservation = Reservation(
            user = user!!,
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
    fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, Reservation>> {
        val reservation = service.findId(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

}

