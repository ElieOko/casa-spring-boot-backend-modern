package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountUserEntity

interface TypeAccountUserRepository : JpaRepository<TypeAccountUserEntity, Long> {
}