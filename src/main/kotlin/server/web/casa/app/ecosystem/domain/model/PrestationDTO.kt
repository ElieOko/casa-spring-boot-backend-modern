package server.web.casa.app.ecosystem.domain.model

import server.web.casa.app.actor.domain.model.UserPerson

data class PrestationDTOMaster(
    val prestation : Prestation,
    val image : List<PrestationImage?>,
    val postBy : String,
    val actor : UserPerson? = null
)
