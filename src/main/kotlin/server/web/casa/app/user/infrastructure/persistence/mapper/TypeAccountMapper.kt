package server.web.casa.app.user.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.user.domain.model.TypeAccount
import server.web.casa.app.user.infrastructure.persistence.TypeAccountEntity

@Component
class TypeAccountMapper(
//    val userMapper: UserMapper
) {
    fun toDomain(typeAccountEntity: TypeAccountEntity) : TypeAccount{
        return TypeAccount(
            typeAccountId = typeAccountEntity.typeAccountId,
            name = typeAccountEntity.name,
        )
    }
    fun toEntity(typeAccount: TypeAccount? = null): TypeAccountEntity{
        return TypeAccountEntity(
            typeAccountId = typeAccount!!.typeAccountId,
            name = typeAccount.name
        )
    }
}