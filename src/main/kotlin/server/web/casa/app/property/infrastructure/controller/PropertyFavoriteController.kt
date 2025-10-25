package server.web.casa.app.property.infrastructure.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import server.web.casa.route.property.PropertyRoute

const val ROUTE_PROPERTY_FAVORITE = PropertyRoute.PROPERTY_FAVORITE

@RestController
@RequestMapping(ROUTE_PROPERTY_FAVORITE)
class PropertyFavoriteController {
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createFavorite(){

    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllFavoriteByUser(){

    }
}