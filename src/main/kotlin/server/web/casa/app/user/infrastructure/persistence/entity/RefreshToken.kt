package server.web.casa.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import kotlin.time.*

@Table(name = "refresh_tokens")
class RefreshToken @OptIn(ExperimentalTime::class) constructor(
    @Id
    @Column("user_id")
    val userId: Long,
    @Column("expires_at")
    val expiresAt: java.time.Instant? = null,
    @Column("hashed_token")
    val hashedToken: String,
    @Column("created_at")
    val createdAt: Instant = Clock.System.now()
)