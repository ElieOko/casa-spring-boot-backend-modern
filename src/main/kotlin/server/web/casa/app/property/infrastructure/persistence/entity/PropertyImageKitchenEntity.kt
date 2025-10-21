package server.web.casa.app.property.infrastructure.persistence.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "property_image_kitchens")
class PropertyImageKitchenEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val propertyImageKitchenId : Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn("property_id")
    @JsonBackReference
    var propertyKitchen : PropertyEntity,
    @Column(name = "name")
    var name : String,
    @Column(name = "path_image")
    var path : String
)
