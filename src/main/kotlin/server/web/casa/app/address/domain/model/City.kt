package server.web.casa.app.address.domain.model

import server.web.casa.app.user.domain.model.User

data class City(
    val cityId : Long,
    val country : Country,
    val name : String,
//    val user: User
)
