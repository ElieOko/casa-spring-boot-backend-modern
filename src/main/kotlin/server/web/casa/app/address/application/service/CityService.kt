package server.web.casa.app.address.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.infrastructure.persistence.mapper.*
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CityService(
    private val repository: CityRepository
) {
    suspend fun saveCity(data: City): City? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend  fun findAllCity() : List<City?> = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdCity(id : Long) : City?  = repository.findById(id).orElse(null).toDomain()
}