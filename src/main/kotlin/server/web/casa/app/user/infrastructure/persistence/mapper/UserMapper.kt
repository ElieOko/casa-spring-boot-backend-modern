package server.web.casa.app.user.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.user.domain.model.*
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.time.ExperimentalTime

@Component
class UserMapper(
    val mapperAccount : TypeAccountMapper
) {

    fun toDomain(entity: UserEntity?) : UserDto? {
        var data : UserDto? = null
        if (entity != null ){
          data = UserDto(
              userId = entity.userId,
              typeAccount = mapperAccount.toDomain(entity.typeAccount),
              email = entity.email,
              phone = entity.phone.toString(),
              username = entity.username.toString(),
              city = entity.city.toString(),
              country = entity.country,
              isPremium = entity.isPremium,
              isCertified = entity.isCertified,
          )
        }
        return data
    }

    @OptIn(ExperimentalTime::class)
    fun toEntity(user: User? = null) : UserEntity? {
        var data : UserEntity? = null
        if (user != null){
           data = UserEntity(
               userId = user.userId,
               password = user.password,
               username = user.username,
               typeAccount = mapperAccount.toEntity(user.typeAccount),
               email = user.email,
               phone = user.phone,
               city = user.city,
               country = user.country
           )
        }
        return data
    }

    @OptIn(ExperimentalTime::class)
    fun toEntityToDto(user: UserDto? = null) : UserEntity? {
        var data : UserEntity? = null
        if (user != null){
            data = UserEntity(
                userId = user.userId,
                username = user.username,
                typeAccount = mapperAccount.toEntity(user.typeAccount),
                email = user.email,
                phone = user.phone,
                city = user.city,
                country = user.country,
            )
        }
        return data
    }
}


