package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import kotlin.time.*

@Table(name = "refresh_tokens")
class RefreshToken @OptIn(ExperimentalTime::class) constructor(
    @Id
    val userId: Long,
    val expiresAt: java.time.Instant?,
    val hashedToken: String,
    val createdAt: Instant = Clock.System.now()
)