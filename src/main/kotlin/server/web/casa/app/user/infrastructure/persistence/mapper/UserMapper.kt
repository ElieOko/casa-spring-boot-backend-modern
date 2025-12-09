package server.web.casa.app.user.infrastructure.persistence.mapper

import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.time.ExperimentalTime


fun UserEntity.toDomain(): UserDto {
    val entity = this
    return UserDto(
        userId = entity.userId,
        typeAccount = entity.typeAccount.toDomain(),
        email = entity.email,
        phone = entity.phone.toString(),
        username = entity.username.toString(),
        city = entity.city.toString(),
        country = entity.country,
        isPremium = entity.isPremium,
        isCertified = entity.isCertified,
    )
}

@OptIn(ExperimentalTime::class)
fun UserDto.toEntityToDto(): UserEntity {
    val user = this
    return UserEntity(
        userId = user.userId,
        username = user.username,
        typeAccount = user.typeAccount!!.toEntity(),
        email = user.email,
        phone = user.phone,
        city = user.city,
        country = user.country,
    )
}

@OptIn(ExperimentalTime::class)
fun User.toEntity(): UserEntity {
    val user = this
    return UserEntity(
        userId = user.userId,
        username = user.username,
        typeAccount = user.typeAccount!!.toEntity(),
        email = user.email,
        phone = user.phone,
        city = user.city,
        country = user.country,
    )
}

