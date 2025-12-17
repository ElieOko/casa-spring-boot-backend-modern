package server.web.casa.app.user.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import kotlin.time.*

@Table(name = "users")
class UserEntity @OptIn(ExperimentalTime::class) constructor(
    @Id
    @Column("id")
    val userId: Long? = null,
    @Column("city")
    var city: String? = null,
    @JsonIgnore
    @Column("password")
    var password: String? = "",
    @Column("email")
    var email: String? = null,
    @Column("username")
    var username: String? = null,
    @Column("is_premium")
    var isPremium: Boolean = false,
    @Column("is_certified")
    var isCertified: Boolean = false,
    @Column("phone")
    val phone: String?=null,
    @Column("country")
    val country: String? = "Democratic Republic of the Congo",
    @Column("created_at")
    val createdAt: Instant = Clock.System.now())
