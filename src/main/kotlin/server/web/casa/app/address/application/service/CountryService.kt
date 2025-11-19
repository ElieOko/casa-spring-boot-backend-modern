package server.web.casa.app.address.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.infrastructure.persistence.mapper.CountryMapper
import server.web.casa.app.address.infrastructure.persistence.repository.CountryRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CountryService(
   private val repository: CountryRepository,
   private val mapper: CountryMapper
) {
    suspend fun saveCountry(data: Country): Country {
        val data = mapper.toEntity(data)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun findAllCountry() : List<Country>{
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
}