package server.web.casa.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.casa.app.address.domain.model.City
import server.web.casa.app.address.domain.model.Commune
import server.web.casa.app.address.domain.model.Country
import server.web.casa.app.address.domain.model.District
import server.web.casa.app.address.domain.model.Quartier
import server.web.casa.app.address.infrastructure.persistence.mapper.CommuneMapper
import server.web.casa.app.address.infrastructure.persistence.mapper.QuartierMapper
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.address.infrastructure.persistence.repository.QuartierRepository

@Service
class QuartierService(
    private val repository: QuartierRepository,
    private val mapper : QuartierMapper
) {
    suspend fun saveQuartier(data: Quartier): Quartier {
        val data = mapper.toEntity(data)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }

    suspend fun findAllQuartier() : List<Quartier>{
        return repository.findAll().map { Quartier(
            quartierId = it.quartierId,
            commune = Commune(
                communeId = it.commune!!.communeId,
                name = it.commune!!.name,
                district = District(
                    districtId = it.commune!!.district!!.districtId,
                    city = City(
                        cityId = it.commune!!.district!!.city.cityId,
                        country = Country(
                            countryId = it.commune!!.district!!.city.country.countryId,
                            name = it.commune!!.district!!.city.country.name
                        ),
                        name = it.commune!!.district!!.city.name
                    ),
                    name = it.commune!!.district!!.name
                )
            ),
            name = it.name
        ) }.toList()
    }

    suspend fun findByIdQuartier(id : Long): Quartier {
        repository.findById(id).let{ return mapper.toDomain(it.orElse(null)) }
    }
}