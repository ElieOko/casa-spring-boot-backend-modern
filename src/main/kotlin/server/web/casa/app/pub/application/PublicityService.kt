package server.web.casa.app.pub.application

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.pub.domain.model.Publicity
import server.web.casa.app.pub.infrastructure.persistance.mapper.PublicityMapper
import server.web.casa.app.pub.infrastructure.persistance.repo.PublicityRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class PublicityService(
    private  val repo: PublicityRepository,
    private val mp: PublicityMapper
) {
    fun createPub(pub: Publicity): Publicity {
        val data = mp.toEntity(pub)
        val result = repo.save(data)
        return mp.toDomain(result)
    }

    fun findAllPub() : List<Publicity?> {
        return repo.findAll().map{ mp.toDomain(it) }.toList()
    }

    fun findId(id: Long): Publicity? {
        val pub = repo.findById(id).orElse(null)
        return pub?.let { mp.toDomain(it) }
    }
    fun updateIsActive(id: Long, state: Boolean): Boolean{
        val pub = repo.findById(id).orElse(null) ?: return false
        pub.isActive = state
        repo.save(pub)
        return true
    }

    fun findByUser(userId: Long): List<Publicity?> {
        return repo.findAll().filter { entity ->
            entity.user?.userId == userId
        }.map{ mp.toDomain(it) }.toList()
    }
    fun deleteById(id: Long): Boolean {
        val pub = repo.deleteById(id)
        return true
    }

}