package server.web.casa.app.ecosystem.domain.request

data class ElectricienServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)

