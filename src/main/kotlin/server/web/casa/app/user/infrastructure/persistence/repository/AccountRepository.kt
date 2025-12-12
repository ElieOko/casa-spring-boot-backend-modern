package server.web.casa.app.user.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import server.web.casa.app.user.infrastructure.persistence.entity.AccountEntity
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity

interface AccountRepository : JpaRepository<AccountEntity, Long>