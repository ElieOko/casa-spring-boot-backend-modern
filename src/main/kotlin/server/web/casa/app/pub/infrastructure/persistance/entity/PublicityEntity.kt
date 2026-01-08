package server.web.casa.app.pub.infrastructure.persistance.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Table("publicities")
data class PublicityEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val user: Long? = null,  // FK vers users.id
    @Column("image_path")
    var imagePath: String? = null,
    @Column("title")
    val title: String,
    @Column("description")
    val description: String,
    @Column("is_active")
    var isActive: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDate = LocalDate.now()
)
