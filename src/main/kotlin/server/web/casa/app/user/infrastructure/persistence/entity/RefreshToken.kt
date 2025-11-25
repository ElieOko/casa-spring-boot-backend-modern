package server.web.casa.app.user.infrastructure.persistence.entity

import jakarta.persistence.*
import kotlin.time.*

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken @OptIn(ExperimentalTime::class) constructor(
    @Id
    @Column("userId")
    val userId: Long,
    @Column(name = "expires_at")
    val expiresAt: java.time.Instant?,
    @Column("hashedToken")
    val hashedToken: String,
    @Column("createdAt")
    val createdAt: Instant = Clock.System.now()
)