package server.web.casa.app.user.infrastructure.persistence.mapper

import org.springframework.stereotype.Component
import server.web.casa.app.address.infrastructure.persistence.mapper.CityMapper
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import kotlin.time.ExperimentalTime

@Component
class UserMapper(
    val mapperAccount : TypeAccountMapper,
    val mapperCity : CityMapper,
) {

    fun toDomain(entity: UserEntity?) : User? {
        var data : User? = null
        if (entity != null ){
          data = User(
                userId = entity.userId,
                password = entity.password.toString(),
                typeAccount = mapperAccount.toDomain(entity.typeAccount) ,
                email = entity.email,
                phone = entity.phone,
                city = mapperCity.toDomain(entity.city)
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
                typeAccount =mapperAccount.toEntity(user.typeAccount) ,
                email = user.email,
                phone = user.phone,
                city = mapperCity.toEntity(user.city)
            )
        }
        return data
    }
}