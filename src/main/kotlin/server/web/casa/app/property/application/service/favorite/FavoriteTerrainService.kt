package server.web.casa.app.property.application.service.favorite

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import server.web.casa.app.property.application.service.TerrainService
import server.web.casa.app.property.domain.model.favorite.FavoriteTerrainDTO
import server.web.casa.app.property.infrastructure.persistence.entity.favorite.FavoriteTerrainEntity
import server.web.casa.app.property.infrastructure.persistence.repository.favorite.FavoriteTerrainRepository
import server.web.casa.app.user.application.service.UserService

class FavoriteTerrainService(
    private val repository: FavoriteTerrainRepository,
    private val userS: UserService,
    private val terS: TerrainService
) {
    suspend fun create(f : FavoriteTerrainEntity): FavoriteTerrainDTO {
        val result = repository.save(f)
        return toFavoriteDTO(result)
    }
    suspend fun getById(id: Long): FavoriteTerrainDTO? {
        val fav = repository.findById(id) ?: return null
        return toFavoriteDTO(fav)
    }
    suspend fun getAll() = repository.findAll().map {
        toFavoriteDTO(it)
    }.toList()

    suspend fun getUserFavorite(user: Long) : List<FavoriteTerrainDTO>{
        return repository.findFavoriteByUserId(user)?.map {
            toFavoriteDTO(it)
        }?.toList() ?: emptyList()
    }

    suspend fun getFavoriteByTerrainId( terId: Long ) : List<FavoriteTerrainDTO>{
        return repository.findFavoriteByTerrain(terId).let {list-> list?.map{
            toFavoriteDTO(it)
        }?.toList() ?: emptyList() }
    }
    suspend fun getFavoriteIfExist( terId: Long , user: Long) : List<FavoriteTerrainDTO>{
        return repository.findFavoriteExist(terId, user).let{list-> list?.map{toFavoriteDTO(it)}?.toList() ?: emptyList() }
    }
    suspend fun deleteById(favoriteId: Long) {
        return repository.deleteById(favoriteId)
    }
    suspend fun deleteAll() {
        return repository.deleteAll()
    }
    suspend fun deleteAllFavoriteUser(user: Long): Boolean {
        val getAll = repository.findAll().filter{ it.userId == user }.toList()
        for (entity in getAll) {
            repository.delete(entity)
        }
        return true
    }

    suspend fun toFavoriteDTO(it: FavoriteTerrainEntity): FavoriteTerrainDTO =
        FavoriteTerrainDTO(
            favorite = it,
            user = userS.findIdUser(it.userId),
            terrain = terS.findById(it.terrainId)
        )

}