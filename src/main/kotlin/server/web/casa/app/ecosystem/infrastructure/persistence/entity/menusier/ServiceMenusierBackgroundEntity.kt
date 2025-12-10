package server.web.casa.app.ecosystem.infrastructure.persistence.entity.menusier

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "service_menusier_backgrounds")
class ServiceMenusierBackgroundEntity(
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val serviceMenusierBackgroundId : Long,
    @ManyToOne
    @JoinColumn("service_menusier_id")
    @JsonBackReference
    var service : ServiceMenusierEntity,
    @Column(name = "name")
    val name : String,
    @Column(name = "path_image")
    val path : String
)