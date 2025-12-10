package server.web.casa.app.ecosystem.domain.request

data class FrigoristeServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)
