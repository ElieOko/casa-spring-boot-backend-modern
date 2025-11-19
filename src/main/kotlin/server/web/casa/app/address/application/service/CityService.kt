package server.web.casa.app.address.application.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.app.address.infrastructure.persistence.mapper.CityMapper
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.utils.Mode

@Service
@Profile(Mode.DEV)
class CityService(
    private val repository: CityRepository,
    private val mapper: CityMapper
) {
    suspend fun saveCity(data: City): City? {
        val data = mapper.toEntity(data)
        val result = repository.save(data as CityEntity)
        return mapper.toDomain(result)
    }
    suspend  fun findAllCity() : List<City?>{
        return repository.findAll().map { mapper.toDomain(it) }.toList()
    }
    suspend fun findByIdCity(id : Long) : City? {
        return repository.findById(id).let{
            mapper.toDomain(it.orElse(null))
        }
    }
}