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
import server.web.casa.app.reservation.domain.model.*
import server.web.casa.app.reservation.domain.model.request.ReservationRequest
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.app.user.infrastructure.persistence.repository.UserRepository
import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.*

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
    private val notif: NotificationReservationService
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: ReservationRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userService.findIdUser(request.userId)
        val property = propertyService.findByIdProperty(request.propertyId)

        if(property.first.property.userId == user.userId){
            val responseOwnProperty = mapOf("error" to "You can't reserve your own property")
            return ResponseEntity.ok().body(responseOwnProperty )
        }
        if (request.endDate < request.startDate){
            val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
            return ResponseEntity.ok().body(responseNotFound )
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
            return ResponseEntity.ok().body(responseHour )
        }

        // check if property is available before adding
        if(!property.first.property.isAvailable){
            val responseAvailable = mapOf("error" to "Unfortunately, this property is already taken.")
            return ResponseEntity.ok().body(responseAvailable)
        }
        val reservationCreate = service.createReservation(dataReservation)
        val notification = notif.create(
            NotificationReservation(
                reservation = reservationCreate.reservation!!,
                guestUser = userEntity,
                hostUser = userR.findById( propertyEntity.user!!)!!
            )
        )
        val response = mapOf(
            "message" to "Votre reservation à la date du ${reservationCreate.reservation.startDate} au ${reservationCreate.reservation.endDate} a été créée avec succès",
            "reservation" to reservationCreate,
            "user" to userEntity,
            "proprietaire" to  userR.findById( propertyEntity.user),
            //"property" to property,
            "notificationSendState" to notification
        )
        return ResponseEntity.status(201).body(response)
    }
    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(): ResponseEntity<Map<String, List<ReservationDTO>>>
    {
        val data = service.findAllReservation()
        val response = mapOf("reservation" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, ReservationDTO?>> {
        val reservation = service.findById(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(@PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationDTO>>> {
        val reservation = service.findByStatus(status)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(@PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationDTO>>> {
        val reservation = service.findByDate(inputDate)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(@PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationDTO>>> {
        val reservation = service.findByMonth(month, year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(@PathVariable year: Int): ResponseEntity<Map<String, List<ReservationDTO>>> {
        val reservation = service.findByPYear(year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(@PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationDTO>?>> {
        val reservation = service.findByInterval(startDateInput, endDateInput)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/user/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByUser(@PathVariable userId: Long): ResponseEntity<out Map<String, Any?>> {

        val user = userR.findById(userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
        //.orElseThrow{ RuntimeException("User not found with id: $userId") }
        val reservation = service.findByUser(user.userId!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/property/{propertyId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(@PathVariable propertyId: Long): ResponseEntity<Map<String, Any?>> {
        val property = propertyR.findById(propertyId) ?: return ResponseEntity.ok(mapOf("error" to "property not found"))//.orElse(null)
        val reservation = service.findByProperty(property.id!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
//        }?: RuntimeException("Property not found with id: $propertyId")
//        val response = mapOf("message" to "Property not found with id: $propertyId")

//        return ResponseEntity.badRequest().body(response)
    }
    @PutMapping("/update/status/{id}")
    suspend fun updateReservation(
        @PathVariable id: Long,
        @RequestBody request:RequestUpdate
    ): ResponseEntity<Map<String, Any?>> {

        val userRequest = userR.findById(request.userId) ?: return ResponseEntity.ok(mapOf("error" to "user not found"))
        val reservation = service.findById(id) ?: return ResponseEntity.ok(mapOf("error" to "reservation not found"))

        val userId = reservation.reservation?.userId
        val proprioId = propertyR.findById( reservation.reservation?.propertyId!!)

        val proprioCheck = userRequest.userId == proprioId?.user
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

    @PutMapping("/notification/partners/{reservationId}")
    suspend fun dealConcludePartners(@PathVariable reservationId: Long): ResponseEntity<Map<String, Any?>> {
        val reservation = service.findById(reservationId)?.reservation
        if (reservation ==null){
            val response = mapOf("error" to "reservation not found")
            return ResponseEntity.ok(response)
        }
        val notification = notif.dealConcludedHost(reservation.id!!, true)
        val notificationGuest = notif.dealConcludedGuest(reservation.id , true)
        val notificationState = notif.stateReservationHost(reservation.id, true)

        val propertyEntity = propertyR.findById(reservation.propertyId!!)
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
        }
        val notification = notif.stateReservationGuestCancel(reservation.id!!)
        val propertyEntity = propertyR.findById(reservation.propertyId!!)
           // .takeIf{ it.isNotEmpty() }!!
           // .filter { entity -> entity!!.propertyId==reservation.property.propertyId }[0] //.filter { }//findById(reservation.property.propertyId)
        propertyEntity!!.isAvailable = true
        propertyR.save(propertyEntity)
        val response = mapOf("DealCancel" to notification, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }
}
class RequestUpdate(
    val status: ReservationStatus,
    val reason: String?,
    //val isActive: Boolean = true,
    val userId: Long
)
