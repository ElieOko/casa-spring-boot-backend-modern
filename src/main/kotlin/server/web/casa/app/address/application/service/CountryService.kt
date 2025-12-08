package server.web.casa.app.address.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.infrastructure.persistence.mapper.*
import server.web.casa.app.address.infrastructure.persistence.repository.CountryRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CountryService(
   private val repository: CountryRepository,
) {
    suspend fun saveCountry(data: Country): Country {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllCountry() = repository.findAll().map { it.toDomain() }.toList()
}