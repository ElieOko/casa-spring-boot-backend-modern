package server.web.casa.app.address.application.service

import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity
import server.web.casa.app.address.infrastructure.persistence.mapper.QuartierMapper
import server.web.casa.app.address.infrastructure.persistence.repository.QuartierRepository

@Service
class QuartierService(
    private val repository: QuartierRepository,
    private val mapper : QuartierMapper
) {
    suspend fun saveQuartier(data: Quartier): Quartier? {
        val data = mapper.toEntity(data)
        val result = repository.save(data as QuartierEntity)
        return mapper.toDomain(result)
    }

    suspend fun findAllQuartier() : List<Quartier>{
        return repository.findAll().map { Quartier(
            quartierId = it.quartierId,
//            commune = Commune(
//                communeId = it.commune!!.communeId,
//                name = it.commune!!.name,
//                district = District(
//                    districtId = it.commune!!.district!!.districtId,
//                    city = City(
//                        cityId = it.commune!!.district!!.city.cityId,
//                        country = Country(
//                            countryId = it.commune!!.district!!.city.country.countryId,
//                            name = it.commune!!.district!!.city.country.name
//                        ),
//                        name = it.commune!!.district!!.city.name
//                    ),
//                    name = it.commune!!.district!!.name
//                )
//            ),
            name = it.name
        ) }.toList()
    }

    suspend fun findByIdQuartier(id : Long): Quartier? {
        repository.findById(id).let{ return mapper.toDomain(it.orElse(null)) }
    }
}