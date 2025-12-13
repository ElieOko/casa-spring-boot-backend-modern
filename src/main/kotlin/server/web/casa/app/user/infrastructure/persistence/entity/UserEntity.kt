package server.web.casa.app.user.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import kotlin.time.*

@Table(name = "users")
class UserEntity @OptIn(ExperimentalTime::class) constructor(
    @Id
    val userId: Long = 0,
    var city: String? = null,
    @JsonIgnore
    var password: String? = "",
    var email: String? = null,
    var username: String? = null,
    var isPremium: Boolean = false,
    var isCertified: Boolean = false,
    val phone: String?=null,
    val country: String? = "Democratic Republic of the Congo",
    val createdAt: Instant = Clock.System.now())
