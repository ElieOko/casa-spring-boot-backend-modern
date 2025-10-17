package server.web.casa.app.actor.domain.model

import server.web.casa.app.user.domain.model.User

data class Locataire(
    val locataireId: Long = 0,
    val firstName  : String,
    val lastName   : String,
    val fullName   : String,
    val address    : String?,
    val images     : String?,
    val cardFront  : String?,
    val cardBack   : String?,
    val user       : User?,
    val typeCard   : TypeCard? = null,
    val numberCard : String? = null
)
