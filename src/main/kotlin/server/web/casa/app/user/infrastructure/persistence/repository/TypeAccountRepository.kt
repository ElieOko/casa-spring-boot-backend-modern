package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity

interface TypeAccountRepository : CoroutineCrudRepository<TypeAccountEntity, Long> {
}