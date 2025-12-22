package server.web.casa.app.pub.application

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.threeten.bp.LocalTime
import server.web.casa.app.pub.infrastructure.persistance.entity.PublicityEntity
import server.web.casa.app.pub.infrastructure.persistance.repository.PublicityRepository
import server.web.casa.utils.Mode
import java.time.LocalDate

@Service
@Profile(Mode.DEV)
class PublicityService(
    private  val repo: PublicityRepository
) {
    suspend fun createPub(pub: PublicityEntity): PublicityEntity {
        return repo.save(pub)
    }
    suspend fun findAllPub() = repo.findAll().map{ it }.toList()

    suspend fun findId(id: Long) = repo.findById(id)

    suspend fun updateIsActive(id: Long, state: Boolean): Boolean{
        val pub = repo.findById(id)
        pub?.isActive = state
        repo.save(pub!!)
        return true
    }

    suspend fun findByUser(userId: Long): List<PublicityEntity?> {
        return repo.findByUserId(userId)?.map{ it }?.toList() ?: emptyList()
    }
    suspend fun deleteById(id: Long): Boolean {
        val pub = repo.deleteById(id)
        return true
    }
    suspend fun findByCreated(date: LocalDate): List<PublicityEntity?>  {
        return repo.findByCreated(date)?.map{ it }?.toList() ?: emptyList()
    }
}