package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.property.application.service.HotelChambreService
import server.web.casa.app.property.application.service.HotelService
import server.web.casa.app.reservation.application.service.ReservationHotelService
import server.web.casa.app.reservation.domain.model.ReservationChambreHotelDTO
import server.web.casa.app.reservation.domain.model.ReservationChambreHotelRequest
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationChambreHotelEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val ROUTE_RESERVATION_HOTEL = ReservationRoute.RESERVATION_HOTEL

@Tag(name = "Reservation", description = "Reservation's Management")
@RestController
@RequestMapping(ROUTE_RESERVATION_HOTEL)
@Profile(Mode.DEV)

class ReservationHotelController(
    private val service: ReservationHotelService,
    private val userS: UserService,
    private val chambrHTL: HotelChambreService,
    private val hotlS: HotelService,
    private val notif: NotificationReservationService
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: ReservationChambreHotelRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val chambrehotel = chambrHTL.getAll().find{ it.id == request.chambreId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "chambre hotel not found"))
        val hotel = hotlS.getAllHotel().find{ it.hotel.id == chambrehotel.hotelId }?.hotel ?: return ResponseEntity.badRequest().body(mapOf("error" to "hotel not found"))

        if(hotel.userId == user.userId){
            val responseOwnProperty = mapOf("error" to "You can't reserve your own hotel")
            return ResponseEntity.ok().body(responseOwnProperty )
        }
        if (request.endDate < request.startDate || request.startDate < LocalDate.now()){
            val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
            return ResponseEntity.ok().body(responseNotFound )
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
            return ResponseEntity.ok().body(responseHour )
        }

        // check if property is available before adding
        if(!hotel.isAvailable){
            val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
            return ResponseEntity.ok().body(responseAvailable)
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
        return ResponseEntity.status(201).body(response)
    }
    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>>
    {
        val data = service.findAllReservation()
        val response = mapOf("reservation" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, ReservationChambreHotelDTO?>> {
        val reservation = service.findById(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(@PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val reservation = service.findByStatus(status)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(@PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val reservation = service.findByDate(inputDate)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(@PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val reservation = service.findByMonth(month, year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(@PathVariable year: Int): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>>> {
        val reservation = service.findByPYear(year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(@PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationChambreHotelDTO>?>> {
        val reservation = service.findByInterval(startDateInput, endDateInput)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByUser(@PathVariable userId: Long): ResponseEntity<out Map<String, Any?>> {

        val user = userS.findIdUser(userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
        //.orElseThrow{ RuntimeException("User not found with id: $userId") }
        val reservation = service.findByUser(user.userId!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/chambre/{hotelId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(@PathVariable hotelId: Long): ResponseEntity<Map<String, Any?>> {
        val property = chambrHTL.getAll().find{ it.id == hotelId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found"))
        // chambrHTL.findById(hotelId) ?: return ResponseEntity.ok(mapOf("error" to "property not found"))//.orElse(null)
        val reservation = service.findByProperty(property.id!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(@PathVariable userId:Long): ResponseEntity<Map<String,  List<ReservationChambreHotelDTO>? >> {
        val user = userS.findIdUser(userId)
        val reservation = service.findByHostUser(user.userId!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)

    }
    @PutMapping("/update/status/{id}")
    suspend fun updateReservation(
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> {

        val userRequest = userS.findIdUser(request.userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
        val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("error" to "reservation not found"))

        val userId = reservation.userId
        val chambrehotel = chambrHTL.getAll().find{ it.id == reservation.chambreId } ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found"))
        val hotel = hotlS.getAllHotel().find{ it.hotel.id == chambrehotel.hotelId }?.hotel ?: return ResponseEntity.badRequest().body(mapOf("error" to "hote not found"))
        val proprioId = hotel.userId
        // chambrHTL.findById( reservation.reservation.hotelId)

        val proprioCheck = userRequest.userId == proprioId
        val emetCheck = userRequest.userId == userId

        if(emetCheck || proprioCheck){
            if (proprioCheck){
                val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                return ResponseEntity.ok(mapOf("reservation" to updated))
            }

            if(request.status != ReservationStatus.APPROVED){
                val updated = service.cancelOrKeepReservation(id, true,request.reason, request.status)
                return ResponseEntity.ok(mapOf("reservation" to updated))
            }
        }
        return ResponseEntity.ok(mapOf("error" to "Authorization denied"))
    }

    @DeleteMapping("/delete/{id}")
    suspend fun deleteReservation(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        val reservation = service.findById(id)?.reservation ?: return ResponseEntity.ok(mapOf("message" to "Reservation not found"))
        val notificationDelete = notif.deleteByReservation(id)
        return if (notificationDelete) {
            service.deleteReservationById(id)
            ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully"))
        }else{
            return ResponseEntity.ok(mapOf("message" to "Something was wrong"))
        }
    }
    @DeleteMapping("/delete/all")
    suspend fun deleteReservationAll(): ResponseEntity<Map<String, String>> {
        val reservation = service.deleteAll()
       return ResponseEntity.ok(mapOf("message" to "Reservation deleted successfully"))
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
