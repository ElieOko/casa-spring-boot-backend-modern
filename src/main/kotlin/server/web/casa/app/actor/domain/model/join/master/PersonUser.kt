package server.web.casa.app.actor.domain.model.join.master

import jakarta.validation.constraints.NotNull
import server.web.casa.app.actor.domain.model.Person
import server.web.casa.app.actor.domain.model.request.PersonRequest
import server.web.casa.app.user.domain.model.User
import server.web.casa.app.user.domain.model.UserRequest
import server.web.casa.app.user.domain.model.request.AccountRequest
import server.web.casa.utils.toPascalCase

data class PersonUserRequest(
    @NotNull
    val user : UserRequest,
    @NotNull
    val account : List<AccountRequest>,
    @NotNull
    val actor : PersonRequest
)

fun PersonUserRequest.toUser(phone: String?) = User(
    password = this.user.password,
    email = this.user.email,
    username = "@"+toPascalCase("${this.actor.firstName} ${this.actor.lastName}"),
    phone = phone,
    city = this.user.city,
    country = this.user.country
)
fun PersonUserRequest.toPerson(userId : Long) = Person(
    firstName = this.actor.firstName,
    lastName = this.actor.lastName,
    fullName = "${this.actor.firstName} ${this.actor.lastName}",
    address = this.actor.address,
    images = this.actor.images,
    cardFront = this.actor.cardFront,
    cardBack = this.actor.cardBack,
    parrainId = this.actor.parrainId ,
    userId = userId,
    typeCardId = this.actor.typeCardId,
    numberCard = this.actor.numberCard,
)

data class PersonUserUpdateRequest(
    @NotNull
    val user : UserRequest,
    @NotNull
    val actor : PersonRequest
)