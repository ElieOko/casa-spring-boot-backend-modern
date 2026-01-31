package server.web.casa.app.actor.domain.model

import server.web.casa.app.user.domain.model.UserDto

data class UserPerson(val user : UserDto, val member : Person?)