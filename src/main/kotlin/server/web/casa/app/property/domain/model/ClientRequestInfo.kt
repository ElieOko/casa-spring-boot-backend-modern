package server.web.casa.app.property.domain.model

data class ClientRequestInfo(
    val ip: String?,
    val userAgent: String?,
    val deviceBrand: String?,
    val deviceModel: String?,
    val os: String?,
    val osVersion: String?,
)