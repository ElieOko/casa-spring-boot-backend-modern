package server.web.casa.app.property.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "properties")
class PropertyEntity(
    @Id
    val id : Long =0,
    val title : String,
    val description : String? = "",
    val price : Double,
    val surface :Double? = null,
    val rooms :Int? = 0,
    val bedrooms : Int? = 0,
    val kitchen : Int? = 0,
    val livingRoom : Int? = 0,
    val bathroom : Int? = 0,
    val electric : Int?=0,
    val water : Int?=0,
    val floor : Int? = 0,
    val address : String,
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    val cityId : Long?,
    val postalCode : String? = "",
    val communeId : Long?,
    val quartierId : Long?,
    val sold : Boolean,
    val transactionType : String,
    val guarantee : String = "",
    val propertyTypeId : Long,
    val user : Long?,
    val latitude : Double? = null,
    val longitude : Double? = null,
    var isAvailable : Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now(),
//    @ManyToMany
//    @JoinTable(
//        "PropertyFeatures",
//        joinColumns = [JoinColumn("property_id")],
//        inverseJoinColumns = [JoinColumn("feature_id")]
//    )
//    @JsonManagedReference
//    val features : List<FeatureEntity> = emptyList(),
)
