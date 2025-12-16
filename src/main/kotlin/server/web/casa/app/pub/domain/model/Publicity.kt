package server.web.casa.app.pub.domain.model

//import jakarta.persistence.Embeddable
import server.web.casa.app.user.domain.model.UserDto
import java.time.LocalDate

data class Publicity(
    val publicityId: Long = 0,
    val user: UserDto?,
    val image: String?,
    val title: String,
    val description: String,
    val isActive: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
)
//@Embeddable
class Image(
    val name: String?,
    val publicPath: String?,
)