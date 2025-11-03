package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.casa.app.address.infrastructure.persistence.entity.QuartierEntity
import server.web.casa.app.reservation.infrastructure.persistence.entity.ReservationEntity
import server.web.casa.app.user.infrastructure.persistence.entity.UserEntity
import java.time.LocalDate

@Entity
@Table(name = "properties")
class PropertyEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyId : Long =0,
    @Column(name = "title")
    val title : String,
    @Column(name = "description", nullable = true)
    val description : String? = "",
    @Column(name = "price")
    val price : Double,
    @Column(name = "surface", nullable = true)
    val surface :Double? = null,
    @Column(name = "rooms", nullable = true)
    val rooms :Int? = 0,
    @Column(name = "bedrooms", nullable = true)
    val bedrooms : Int? = 0,
    @Column(name = "kitchen", nullable = true)
    val kitchen : Int? = 0,
    @Column(name = "livingRoom", nullable = true)
    val livingRoom : Int? = 0,
    @Column(name = "bathroom", nullable = true)
    val bathroom : Int? = 0,
    @Column(name = "electric")
    val electric : Int = 1,
    @Column(name = "floor", nullable = true)
    val floor : Int? = 0,
    @Column(name = "address")
    val address : String,
    @ManyToOne
    @JoinColumn("city_id")
    val city : CityEntity,
    @Column(name = "postalCode", nullable = true)
    val postalCode : String? = "",
    @ManyToOne
    @JoinColumn("commune_id")
    val commune : CommuneEntity,
    @ManyToOne
    @JoinColumn("quartier_id")
    val quartier : QuartierEntity,
    @Column(name = "sold")
    val sold : Boolean,
    @Column(name = "transactionType")
    val transactionType : String,
    @ManyToOne
    @JoinColumn("property_type_id")
    val propertyType : PropertyTypeEntity,
    @ManyToOne
    @JoinColumn("user_id")
    val user : UserEntity?,
    @Column(name = "latitude", nullable = true)
    val latitude : Double? = null,
    @Column(name = "longitude", nullable = true)
    val longitude : Double? = null,
    @Column(name = "isAvailable")
    val isAvailable : Boolean = true,
    @Column("createdAt")
    val createdAt: LocalDate = LocalDate.now(),
    @Column("updatedAt")
    val updatedAt: LocalDate = LocalDate.now(),
    @OneToMany(mappedBy = "property", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val propertyImage : MutableSet<PropertyImageEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyRoom", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val propertyImageRoom : MutableSet<PropertyImageRoomEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyLiving", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val propertyImageLivingRoom : MutableSet<PropertyImageLivingRoomEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyKitchen", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val propertyImageKitchen : MutableSet<PropertyImageKitchenEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "property")
    val reservation : List<ReservationEntity> = emptyList(),
//    @OneToMany(mappedBy = "property")
//    val propertyFeature: MutableSet<PropertyFeatureEntity> = mutableSetOf(),
    @ManyToMany
    @JoinTable(
        "PropertyFeatures",
        joinColumns = [JoinColumn("property_id")],
        inverseJoinColumns = [JoinColumn("feature_id")]
    )
    @JsonManagedReference
    val features : List<FeatureEntity> = emptyList(),
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name = "STUDENT_COURSE_TABLE",
//        joinColumns = {
//            @JoinColumn(name = "student_id", referencedColumnName = "id")
//        },
//        inverseJoinColumns = {
//            @JoinColumn(name = "course_id", referencedColumnName = "id")
//        }
//    )
//    @JsonManagedReference
//    private Set<Course> courses;
//    @ManyToMany
//    @JoinTable(
//        "PropertyFavorites",
//        joinColumns = [JoinColumn("property_id")],
//        inverseJoinColumns = [JoinColumn("user_id")]
//    )
//    val favorites : List<PropertyFavoriteEntity?> = emptyList()
)
