package server.web.casa.app.user.infrastructure.persistence.mapper

import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.entity.TypeAccountEntity

fun TypeAccountEntity.toDomain(): TypeAccount {
    val e = this
    return TypeAccount(
        typeAccountId = e.id,
        name = e.name,
    )
}

fun TypeAccount.toEntity(): TypeAccountEntity {
    val e = this
    return TypeAccountEntity(
        id = e.typeAccountId,
        name = e.name
    )
}