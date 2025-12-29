package server.web.casa.app.ecosystem.domain.model

data class PrestationDTOMaster(
    val prestation : Prestation,
    val image : List<PrestationImage?>,
    val postBy : String,
)
