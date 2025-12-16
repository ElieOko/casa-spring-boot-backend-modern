package server.web.casa.app.property.application.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.infrastructure.persistence.mapper.*
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository

@Service
class PropertyService(
    private val repository: PropertyRepository,

) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p : Property): Property {
        val data = p.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll(
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ) : Page<Property> {
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,repository.findAll().count(),sort)
        val page = repository.findAll(pageable)
        val data = page.map { it.toDomain() }
        return data
    }

    suspend fun getAllPropertyByUser(userId : Long): Flow<Property> {
        val data = repository.findAll().filter { it.user == userId }
        return data.map { it.toDomain() }
    }

    suspend fun findByIdProperty(id: Long): Pair<Property, Flow<Property>> {
        val property = repository.findById(id)?: throw
                ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette proprièté n'existe pas")

        val data = property.toDomain()
        //|| (it.propertyType == data.propertyType)
        var similary = repository.findAll()
            .filter { it.cityValue == data.cityValue }
            .filter { it.communeValue == data.communeValue }
            .filter { it.transactionType == data.transactionType }
            .filter { it.id != data.propertyId }
        if (data.city?.cityId == 1L){
            similary = repository.findAll()
                .filter { it.cityId == 1L }
                .filter {it.communeId == data.commune?.communeId}
                .filter {it.transactionType == data.transactionType }
                .filter { it.id != data.propertyId }
//
//                .filter { it.commune?.communeId == data.commune?.communeId}
//                .filter { it.propertyId != data.propertyId }
//                .filter {
//                    it.transactionType == data.transactionType
//                }
        }

        val key = Pair(data,similary.map{it.toDomain()})
        return key
    }
    suspend fun filterProduct(
        filterModel : PropertyFilter,
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ): Page<Property>{
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,size,sort)
        val data = repository.filterProperty(
            transactionType = filterModel.transactionType,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            room = filterModel.room,
            pageable = pageable
        )
        return data.map { it.toDomain() }
    }
}

