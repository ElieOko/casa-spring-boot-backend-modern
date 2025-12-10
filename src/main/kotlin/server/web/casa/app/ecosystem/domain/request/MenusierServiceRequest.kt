package server.web.casa.app.ecosystem.domain.request

data class MenusierServiceRequest(
    val service : TaskRequest,
    val realisation : List<ImageRequest>
)

