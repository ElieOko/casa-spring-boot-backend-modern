package server.web.casa.app.notification.application.service

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.notification.domain.model.request.NotificationReservation
import server.web.casa.app.notification.domain.model.request.NotificationReservationBureau
import server.web.casa.app.notification.domain.model.request.NotificationReservationFestive
import server.web.casa.app.notification.domain.model.request.NotificationReservationFuneraire
import server.web.casa.app.notification.domain.model.request.NotificationReservationHotel
import server.web.casa.app.notification.domain.model.request.NotificationReservationTerrain
import server.web.casa.app.notification.domain.model.request.NotificationReservationVacance
import server.web.casa.app.notification.domain.model.request.TagType
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationCasaEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationBureauEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationFestiveEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationFuneraireEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationHotelEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationTerrainEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.NotificationReservationVacanceEntity
import server.web.casa.app.notification.infrastructure.persistence.entity.toDomain
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationCasaRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationBureauRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationFestiveRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationFuneraireRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationHotelRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationTerrainRepository
import server.web.casa.app.notification.infrastructure.persistence.repository.NotificationReservationVacanceRepository
import server.web.casa.app.reservation.application.service.ReservationService
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.mapOf

@Service
class  NotificationReservationService(
    private val repository: NotificationReservationRepository,
    private val repository2: NotificationReservationFestiveRepository,
    private val repository3: NotificationReservationFuneraireRepository,
    private val repository4: NotificationReservationHotelRepository,
    private val repository5: NotificationReservationTerrainRepository,
    private val repository6: NotificationReservationVacanceRepository,
    private val repository1: NotificationReservationBureauRepository,
    private val reservationService: ReservationService,
    private val notificationService: NotificationService,
    private val notification : NotificationCasaRepository,
) {
    /*
    @property notification
    **/
    suspend fun create(notice: NotificationReservation): Boolean {
       repository.save(NotificationReservationEntity(
           reservationId = notice.reservation.id!!,
           guestUserId = notice.guestUser.userId!!,
           hostUserId = notice.hostUser.userId!!,
           guestUserState = true
       )).let {
           return true
       }
    }
    suspend fun dealConcludedHost(reservation : Long, state : Boolean): Map<String, Any> {
      val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuest(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHost(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository.save(data)
        return state
    }
    suspend fun stateReservationGuestCancel(reservation : Long): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository.save(data)
        return false
    }

    /*
    @Bureau notification
    **/
    suspend fun createBureau(notice: NotificationReservationBureau): Boolean {
        repository1.save(NotificationReservationBureauEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostBureau(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository1.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository1.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestBureau(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository1.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository1.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostBureau(reservation : Long, state : Boolean): Boolean {
        val data = repository1.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository1.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelBureau(reservation : Long): Boolean {
        val data = repository1.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository1.save(data)
        return false
    }

    /*
     @Festive notification
    **/
    suspend fun createFestive(notice: NotificationReservationFestive): Boolean {
        repository2.save(NotificationReservationFestiveEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostFestive(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository2.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository2.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestFestive(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository2.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository2.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostFestive(reservation : Long, state : Boolean): Boolean {
        val data = repository2.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository2.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelFestive(reservation : Long): Boolean {
        val data = repository2.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository2.save(data)
//        val note = notification.save(
//            NotificationCasaEntity(
//                id = null,
//                userId = data.hostUserId,
//                title = "Annulation de visite",
//                message = "Le client a annulé sa demande de visite. Aucune action n’est requise.",
//                tag = TagType.DEMANDES.toString(),
//            )
//        )
//        notificationService.sendNotificationToUser(data.hostUserId.toString(),note.toDomain())
        return false
    }

    /*
     @Funeraire notification
     **/
    suspend fun createFuneraire(notice: NotificationReservationFuneraire): Boolean {
        repository3.save(NotificationReservationFuneraireEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostFuneraire(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository3.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository3.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestFuneraire(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository3.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository3.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostFuneraire(reservation : Long, state : Boolean): Boolean {
        val data = repository3.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository3.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelFuneraire(reservation : Long): Boolean {
        val data = repository3.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository3.save(data)
        return false
    }

    /*
    @Hotel notification
    **/
    suspend fun createHotel(notice: NotificationReservationHotel): Boolean {
        repository4.save(NotificationReservationHotelEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostHotel(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository4.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository4.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestHotel(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository4.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository4.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostHotel(reservation : Long, state : Boolean): Boolean {
        val data = repository4.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository4.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelHotel(reservation : Long): Boolean {
        val data = repository4.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository4.save(data)
        return false
    }

    /*
     @Vacance notification
    */
    suspend fun createVacance(notice: NotificationReservationVacance): Boolean {
        repository6.save(NotificationReservationVacanceEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostVacance(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository6.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository6.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestVacance(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository6.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository6.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostVacance(reservation : Long, state : Boolean): Boolean {
        val data = repository.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelVacance(reservation : Long): Boolean {
        val data = repository6.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository6.save(data)
        return false
    }
    /*
    @Terrain notification
     */
    suspend fun createTerrain(notice: NotificationReservationTerrain): Boolean {
        repository5.save(NotificationReservationTerrainEntity(
            reservationId = notice.reservation.id!!,
            guestUserId = notice.guestUser.userId!!,
            hostUserId = notice.hostUser.userId!!,
            guestUserState = true
        )).let {
            return true
        }
    }
    suspend fun dealConcludedHostTerrain(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository5.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserDealConcluded = state
        repository5.save(data)
        return mapOf("state" to state, "host" to data.hostUserId)
    }
    suspend fun dealConcludedGuestTerrain(reservation : Long, state : Boolean): Map<String, Any> {
        val data = repository5.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserDealConcluded = state
        repository5.save(data)
        return mapOf("state" to state, "guest" to data.guestUserId)
    }
    suspend fun stateReservationHostTerrain(reservation : Long, state : Boolean): Boolean {
        val data = repository5.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.hostUserState = state
        repository5.save(data)
        return state
    }
    suspend fun stateReservationGuestCancelTerrain(reservation : Long): Boolean {
        val data = repository5.findAll().filter { entity ->
            entity.reservationId == reservation
        }.first()
        data.guestUserState = false
        repository5.save(data)
        return false
    }

    suspend fun deleteByReservation(reservation: Long): Boolean {
        val notificationDelete = repository.findAll().filter { entity -> entity.reservationId == reservation }.toList()
        repository.deleteAll(notificationDelete)
        return notificationDelete.isNotEmpty()
    }
}