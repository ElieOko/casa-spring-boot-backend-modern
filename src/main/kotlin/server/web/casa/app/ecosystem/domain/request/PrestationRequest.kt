package server.web.casa.app.ecosystem.domain.request

import server.web.casa.app.ecosystem.domain.model.Prestation
import server.web.casa.app.ecosystem.domain.model.PrestationDTO

data class PrestationRequest(
    val prestation : PrestationDTO,
    val images : List<ImageRequest>
)

data class ImageRequest(
    val image : String = ""
)