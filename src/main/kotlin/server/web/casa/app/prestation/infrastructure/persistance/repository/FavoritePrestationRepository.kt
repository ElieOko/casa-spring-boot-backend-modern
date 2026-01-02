package server.web.casa.app.prestation.infrastructure.persistance.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.prestation.infrastructure.persistance.entity.FavoritePrestationEntity

interface FavoritePrestationRepository  : CoroutineCrudRepository<FavoritePrestationEntity, Long> {}