package server.web.casa.app.property.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import server.web.casa.app.actor.infrastructure.persistence.repository.PersonRepository
import server.web.casa.app.address.application.service.*
import server.web.casa.app.payment.application.service.DeviseService
import server.web.casa.app.property.domain.model.Terrain
import server.web.casa.app.property.domain.model.dto.*
import server.web.casa.app.property.domain.model.dto.toDTO
import server.web.casa.app.property.domain.model.filter.PropertyFilter
import server.web.casa.app.property.domain.model.toEntity
import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.property.infrastructure.persistence.repository.*
import server.web.casa.app.user.application.service.UserService
import kotlin.collections.*
import kotlin.collections.get
import kotlin.collections.map

@Service
class TerrainService(
    private val repository: TerrainRepository,
    private val terrainImage: TerrainImageRepository,
    private val devise: DeviseService,
    private val cityService: CityService,
    private val communeService: CommuneService,
    private val quartierService: QuartierService,
    private val person : PersonRepository,
    private val userService: UserService,
    private val propertyTypeService: PropertyTypeService,
) {
    private suspend fun findAll(data: List<TerrainEntity>) = coroutineScope {
        val terrain = mutableListOf<TerrainMasterDTO>()
        val bureauIds: List<Long> = data.map { it.id!! }
        val images = terrainImage.findByTerrainIdIn(bureauIds).toList()
        val imageByTerrain: Map<Long, List<TerrainImageEntity>> = images.groupBy { it.terrainId }
        data.forEach { m->
            terrain.add(
                TerrainMasterDTO(
                    terrain = m.toDomain().toDTO(),
                    images = imageByTerrain[m.id]?.map { it.toDomain() } ?: emptyList(),
                    devise = devise.getById(m.deviseId!!),
                    address = m.toAddressDTO(),
                    image = person.findByUser(m.userId)?.images?:"",
                    localAddress = LocalAddressDTO(
                        city = cityService.findByIdCity(m.cityId),
                        commune = communeService.findByIdCommune(m.communeId),
                        quartier = quartierService.findByIdQuartier(m.quartierId)
                    ),
                    geoZone = m.toGeo(),
                    postBy = userService.findIdUser(m.userId).username,
                    typeProperty = propertyTypeService.findByIdPropertyType(m.propertyTypeId?:0),
                )
            )
        }
        terrain
    }
    suspend fun getAll() = coroutineScope{
        val data =  repository.findAll().toList()
        findAll(data)
    }
    suspend fun getAllPropertyByUser(userId : Long) = coroutineScope{
        val data = repository.findAllByUser(userId).toList()
        findAll(data)
    }
    suspend fun create(data : Terrain) = coroutineScope {
        repository.save(data.toEntity()).toDomain()
    }
    suspend fun findById(id : Long) = coroutineScope {
        repository.findById(id)?.toDomain()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cette proprièté n'existe pas le terrain.")
    }
    suspend fun getImageByTerrainID( terrainId: Long) = coroutineScope { terrainImage.findByTerrainId(terrainId).toList() }

    suspend fun createOrUpdate(model : Terrain) =  coroutineScope{
        repository.save(model.toEntity())
    }
    suspend fun update(p: Terrain) = coroutineScope {
        val data = p.toEntity()
        val result = repository.save(data)
        result.toDomain()
    }
    suspend fun filter(filterModel : PropertyFilter) = coroutineScope {
        val terrain = mutableListOf<TerrainMasterDTO>()
        val data = repository.filter(
            transactionType = filterModel.transactionType,
            minPrice = filterModel.minPrice,
            maxPrice = filterModel.maxPrice,
            city = filterModel.city,
            commune = filterModel.commune,
            typeMaison = filterModel.typeMaison,
            cityValue = filterModel.cityValue,
            communeValue = filterModel.communeValue,
        ).toList()
        val bureauIds: List<Long> = data.map { it.id!! }
        val images = terrainImage.findByTerrainIdIn(bureauIds).toList()
        val imageByTerrain: Map<Long, List<TerrainImageEntity>> = images.groupBy { it.terrainId }

        data.forEach { m->
            terrain.add(
                TerrainMasterDTO(
                    terrain = m.toDomain().toDTO(),
                    devise = devise.getById(m.deviseId!!),
                    postBy = userService.findIdUser(m.userId).username,
                    address = m.toAddressDTO(),
                    image = person.findByUser(m.userId)?.images?:"",
                    localAddress = LocalAddressDTO(
                        city = cityService.findByIdCity(m.cityId),
                        commune = communeService.findByIdCommune(m.communeId),
                        quartier = quartierService.findByIdQuartier(m.quartierId)
                    ),
                    geoZone = m.toGeo(),
                    images = imageByTerrain[m.id]?.map { it.toDomain() } ?: emptyList(),
                    typeProperty = propertyTypeService.findByIdPropertyType(m.propertyTypeId?:0),
                )
            )
        }
        terrain
    }
}