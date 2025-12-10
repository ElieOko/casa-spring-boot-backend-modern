package server.web.casa.app.ecosystem.domain.request

data class CarreleurServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)
