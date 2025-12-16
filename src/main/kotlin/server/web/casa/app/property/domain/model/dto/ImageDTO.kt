package server.web.casa.app.property.domain.model.dto

import server.web.casa.app.property.infrastructure.persistence.entity.*
import server.web.casa.app.pub.domain.model.Image

data class ImageDTO(
    val main : List<PropertyImageEntity?>,
    val room : List<PropertyImageRoomEntity?>,
    val living : List<PropertyImageLivingRoomEntity?>,
    val kitchen: List<PropertyImageKitchenEntity?>,
)

data class Images(
    val main : List<Image?>,
    val room : List<Image?>,
    val living : List<Image?>,
    val kitchen: List<Image?>,
)

fun ImageDTO.toImage() = Images(
    main = this.main.map { Image(it?.name,it?.path) }.toList(),
    room = this.room.map { Image(it?.name,it?.path) }.toList(),
    living = this.living.map { Image(it?.name,it?.path) }.toList(),
    kitchen = this.kitchen.map { Image(it?.name,it?.path) }.toList()
)
