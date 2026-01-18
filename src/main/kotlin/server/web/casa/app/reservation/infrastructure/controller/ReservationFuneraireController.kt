package server.web.casa.app.reservation.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import server.web.casa.app.notification.application.service.NotificationReservationService
import server.web.casa.app.property.application.service.SalleFuneraireService
import server.web.casa.app.reservation.application.service.ReservationFuneraireService
import server.web.casa.app.reservation.domain.model.ReservationFuneraireDTO
import server.web.casa.app.reservation.domain.model.ReservationFuneraireRequest
import server.web.casa.app.reservation.domain.model.ReservationStatus
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationFuneraireEntity
import server.web.casa.app.user.application.service.UserService
import server.web.casa.route.reservation.ReservationRoute
import server.web.casa.utils.Mode
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val ROUTE_RESERVATION_FUNERAIRE = ReservationRoute.RESERVATION_FUNERAIRE

@Tag(name = "Reservation", description = "Reservation's Management")
@RestController
@RequestMapping(ROUTE_RESERVATION_FUNERAIRE)
@Profile(Mode.DEV)

class ReservationFuneraireController(
    private val service: ReservationFuneraireService,
    private val userS: UserService,
    private val funerS: SalleFuneraireService,
    private val notif: NotificationReservationService
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        @Valid @RequestBody request: ReservationFuneraireRequest
    ): ResponseEntity<Map<String, Any?>> {
        val user = userS.findIdUser(request.userId)
        val bureau = funerS.findById(request.funeraireId)

        if(bureau.userId == user.userId){
            val responseOwnProperty = mapOf("error" to "You can't reserve your own property")
            return ResponseEntity.ok().body(responseOwnProperty )
        }
        if (request.endDate < request.startDate){
            val responseNotFound = mapOf("error" to "End date must be after or equal to start date")
            return ResponseEntity.ok().body(responseNotFound )
        }
        val dataReservation = ReservationFuneraireEntity(
            type = request.type.toString(),
            isActive = true,
            reservationHeure = request.reservationHeure,
            userId = request.userId,
            funeraireId = bureau.id!!,
            message = request.message,
            startDate = request.startDate,
            endDate = request.endDate,
        )
        //!= verify
        //val propertyEntity = funerS.findById(request.funeraireId)
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
            return ResponseEntity.ok().body(responseHour )
        }

        // check if property is available before adding
        if(!bureau.isAvailable){
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
            "message" to "Votre reservation à la date du ${reservationCreate.reservation?.startDate} au ${reservationCreate.reservation?.endDate} a été créée avec succès",
            "reservation" to reservationCreate,
            "proprietaire" to  userS.findIdUser( bureau.userId!!),
            //"property" to property,
           // "notificationSendState" to notification
        )
        return ResponseEntity.status(201).body(response)
    }
    @GetMapping("/",produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getAllReservation(): ResponseEntity<Map<String, List<ReservationFuneraireDTO>>>
    {
        val data = service.findAllReservation()
        val response = mapOf("reservation" to data)
        return ResponseEntity.ok().body(response)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
     suspend fun getReservationById(@PathVariable id: Long): ResponseEntity<Map<String, ReservationFuneraireDTO?>> {
        val reservation = service.findById(id)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/status/{status}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByStaus(@PathVariable status: ReservationStatus): ResponseEntity<Map<String, List<ReservationFuneraireDTO>>> {
        val reservation = service.findByStatus(status)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }
    @GetMapping("/date/{inputDate}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByDate(@PathVariable inputDate: LocalDate): ResponseEntity<Map<String, List<ReservationFuneraireDTO>>> {
        val reservation = service.findByDate(inputDate)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/month/{month}/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByMonthYear(@PathVariable month: Int, @PathVariable year: Int): ResponseEntity<Map<String, List<ReservationFuneraireDTO>>> {
        val reservation = service.findByMonth(month, year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/year/{year}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByYear(@PathVariable year: Int): ResponseEntity<Map<String, List<ReservationFuneraireDTO>>> {
        val reservation = service.findByPYear(year)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/interval/{startDateInput}/{endDateInput}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationInInterval(@PathVariable startDateInput: LocalDate, @PathVariable endDateInput: LocalDate): ResponseEntity<Map<String, List<ReservationFuneraireDTO>?>> {
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

    @GetMapping("/bureau/{funeraireId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getReservationByProperty(@PathVariable funeraireId: Long): ResponseEntity<Map<String, Any?>> {
        val property = funerS.findById(funeraireId) ?: return ResponseEntity.ok(mapOf("error" to "property not found"))//.orElse(null)
        val reservation = service.findByProperty(property.id!!)
        val response = mapOf("reservation" to reservation)
        return ResponseEntity.ok(response)
//        }?: RuntimeException("Property not found with id: $funeraireId")
//        val response = mapOf("message" to "Property not found with id: $funeraireId")

//        return ResponseEntity.badRequest().body(response)
    }

    @GetMapping("/user/host/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findByHostUser(@PathVariable userId:Long): ResponseEntity<Map<String,  List<ReservationFuneraireDTO>? >> {
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
        val reservation = service.findById(id) ?: return ResponseEntity.ok(mapOf("error" to "reservation not found"))

        val userId = reservation.reservation.userId
        val proprioId = funerS.findById( reservation.reservation.funeraireId!!)

        val proprioCheck = userRequest.userId == proprioId.userId
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

        val propertyEntity = funerS.findById(reservation.funeraireId!!)
             propertyEntity!!.isAvailable = false
        funerS.save(propertyEntity)*/
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
        val propertyEntity = funerS.findById(reservation.funeraireId!!)
           // .takeIf{ it.isNotEmpty() }!!
           // .filter { entity -> entity!!.funeraireId==reservation.property.funeraireId }[0] //.filter { }//findById(reservation.property.funeraireId)
        propertyEntity!!.isAvailable = true
        funerS.save(propertyEntity)*/
        val response = mapOf("DealCancel" to true, "message" to "Deal cancel successfully")
        return ResponseEntity.ok(response)
    }*/
}
