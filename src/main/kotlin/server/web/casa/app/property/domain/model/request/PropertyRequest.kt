package server.web.casa.app.property.domain.model.request

import jakarta.validation.constraints.NotNull
import server.web.casa.app.property.domain.model.Feature
import server.web.casa.app.property.domain.model.FeatureRequest

data class PropertyRequest(
    @NotNull
    val title : String,
    val description : String? = "",
    @NotNull
    val price : Double,
    val surface : Double? = null,
    val rooms : Int? = 0,
    val bedrooms : Int? = 0,
    val kitchen : Int? = 0,
    val livingRoom : Int? = 0,
    val bathroom : Int? = 0,
    val floor : Int? = 0,
    val address : String,
    val guarantee : String = "",
    val communeValue: String? = "",
    val quartierValue: String? = "",
    val cityValue: String? = "",
    val countryValue: String? = "",
    @NotNull
    val cityId : Long,
    @NotNull
    val communeId : Long,
    @NotNull
    val quartierId : Long,
    @NotNull
    val electric : Int?=0,
    @NotNull
    val water : Int?=0,
    val postalCode : String?,
    val quartier : String,
    val sold : Boolean,
    @NotNull
    val transactionType : String,
    @NotNull
    val propertyTypeId : Long,
    @NotNull
    val userId : Long,
    val latitude : Double? = null,
    val longitude : Double? = null,
    val features : List<FeatureRequest> = emptyList(),
    val propertyImage : List<PropertyImageRequest?> = emptyList(),
    val propertyImageRoom : List<PropertyImageRequest?> = emptyList(),
    val propertyImageLivingRoom : List<PropertyImageRequest?> = emptyList(),
    val propertyImageKitchen : List<PropertyImageRequest?> = emptyList()
)