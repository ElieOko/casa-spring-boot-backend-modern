package server.web.casa.app.pub.application

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.pub.domain.model.Publicity
import server.web.casa.app.pub.infrastructure.persistance.mapper.*
import server.web.casa.app.pub.infrastructure.persistance.repo.PublicityRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class PublicityService(
    private  val repo: PublicityRepository
) {
    fun createPub(pub: Publicity): Publicity {
        val data = pub.toEntity()
        val result = repo.save(data)
        return result.toDomain()
    }
    fun findAllPub() = repo.findAll().map{ it.toDomain() }.toList()

    fun findId(id: Long) = repo.findById(id).orElse(null).toDomain()

    fun updateIsActive(id: Long, state: Boolean): Boolean{
        val pub = repo.findById(id).orElse(null) ?: return false
        pub.isActive = state
        repo.save(pub)
        return true
    }

    fun findByUser(userId: Long): List<Publicity?> {
        return repo.findAll().filter { entity ->
            entity.user?.userId == userId
        }.map{ it.toDomain() }.toList()
    }
    fun deleteById(id: Long): Boolean {
        val pub = repo.deleteById(id)
        return true
    }
}