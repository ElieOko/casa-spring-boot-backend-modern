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
    @Column(name = "electric", nullable = true)
    val electric : Int?=0,
    @Column(name = "water", nullable = true)
    val water : Int?=0,
    @Column(name = "floor", nullable = true)
    val floor : Int? = 0,
    @Column(name = "address")
    val address : String,
    @Column(name = "communeValue", nullable = true)
    val communeValue: String? = "",
    @Column(name = "quartierValue", nullable = true)
    val quartierValue: String? = "",
    @Column(name = "cityValue", nullable = true)
    val cityValue: String? = "",
    @Column(name = "countryValue", nullable = true)
    val countryValue: String? = "",
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn("city_id", nullable = true)
    val city : CityEntity?,
    @Column(name = "postalCode", nullable = true)
    val postalCode : String? = "",
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn("commune_id", nullable = true)
    val commune : CommuneEntity?,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn("quartier_id", nullable = true)
    val quartier : QuartierEntity?,
    @Column(name = "sold")
    val sold : Boolean,
    @Column(name = "transactionType")
    val transactionType : String,
    @Column(name = "guarantee")
    val guarantee : String = "",
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn("property_type_id")
    val propertyType : PropertyTypeEntity,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn("user_id")
    val user : UserEntity?,
    @Column(name = "latitude", nullable = true)
    val latitude : Double? = null,
    @Column(name = "longitude", nullable = true)
    val longitude : Double? = null,
    @Column(name = "isAvailable")
    var isAvailable : Boolean = true,
    @Column("createdAt")
    val createdAt: LocalDate = LocalDate.now(),
    @Column("updatedAt")
    val updatedAt: LocalDate = LocalDate.now(),
    @OneToMany(mappedBy = "property")
    @JsonManagedReference
    val propertyImage : MutableSet<PropertyImageEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyRoom")
    @JsonManagedReference
    val propertyImageRoom : MutableSet<PropertyImageRoomEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyLiving")
    @JsonManagedReference
    val propertyImageLivingRoom : MutableSet<PropertyImageLivingRoomEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "propertyKitchen")
    @JsonManagedReference
    val propertyImageKitchen : MutableSet<PropertyImageKitchenEntity> = mutableSetOf(),
    @OneToMany(mappedBy = "property")
    val reservation : List<ReservationEntity> = emptyList(),
    @ManyToMany
    @JoinTable(
        "PropertyFeatures",
        joinColumns = [JoinColumn("property_id")],
        inverseJoinColumns = [JoinColumn("feature_id")]
    )
    @JsonManagedReference
    val features : List<FeatureEntity> = emptyList(),
)
