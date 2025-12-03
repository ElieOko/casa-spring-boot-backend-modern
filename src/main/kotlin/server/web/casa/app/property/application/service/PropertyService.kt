package server.web.casa.app.property.application.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.util.Streamable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.property.domain.model.Property
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.infrastructure.persistence.entity.PropertyEntity
import server.web.casa.app.property.infrastructure.persistence.mapper.PropertyMapper
import server.web.casa.app.property.infrastructure.persistence.repository.PropertyRepository

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val mapper : PropertyMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun create(p : Property): Property {
        val data = mapper.toEntity(p)
        val result = repository.save(data)
        return mapper.toDomain(result)
    }
    suspend fun getAll(
        page : Int,
        size : Int,
        sortBy : String,
        sortOrder : String
    ) : Page<Property> {
        val sort = if (sortOrder.equals("desc",true)) Sort.by(sortBy).descending()  else Sort.by(sortBy).ascending()
        val pageable = PageRequest.of(page,repository.findAll().size,sort)
        val page = repository.findAll(pageable)
        val data = page.map { mapper.toDomain(it) }
        return data
    }

    suspend fun getAllPropertyByUser(userId : Long) : List<Property> {
        val data = repository.findAll().filter { it.user?.userId == userId }
        return data.map { mapper.toDomain(it) }
    }

    suspend fun findByIdProperty(id: Long): Pair<Property, List<Property>> {
        val property = repository.findById(id).orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST,"Cette proprièté n'existe pas")
            }
        val data = mapper.toDomain(property)
        //|| (it.propertyType == data.propertyType)
        var similary = repository.findAll()
            .filter { it.cityValue == data.cityValue }
            .filter { it.communeValue == data.communeValue }
            .filter { it.transactionType == data.transactionType }
            .filter { it.propertyId != data.propertyId }
        if (data.city?.cityId == 1L){
            similary = repository.findAll()
                .filter { it.city?.cityId == 1L }
                .filter {it.commune?.communeId == data.commune?.communeId}
                .filter {it.transactionType == data.transactionType }
                .filter { it.propertyId != data.propertyId }
//
//                .filter { it.commune?.communeId == data.commune?.communeId}
//                .filter { it.propertyId != data.propertyId }
//                .filter {
//                    it.transactionType == data.transactionType
//                }
        }

        val key = Pair(data,similary.map{mapper.toDomain(it)})
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
        return data.map { mapper.toDomain(it) }
    }
}

