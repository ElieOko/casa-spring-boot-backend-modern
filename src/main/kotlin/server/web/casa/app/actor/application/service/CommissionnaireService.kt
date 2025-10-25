package server.web.casa.app.actor.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.actor.domain.model.Commissionnaire
import server.web.casa.app.actor.infrastructure.persistence.mapper.CommissionnaireMapper
import server.web.casa.app.actor.infrastructure.persistence.repository.CommissionnaireRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CommissionnaireService(
    private val repository: CommissionnaireRepository,
    private val mapper: CommissionnaireMapper
) {
    suspend fun createCommissionnaire(commissionnaire: Commissionnaire): Commissionnaire {
        val data = mapper.toEntity(commissionnaire)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllCommissionnaire() : List<Commissionnaire> {
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
}