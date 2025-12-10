package server.web.casa.app.ecosystem.domain.request

data class PeintreServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)

