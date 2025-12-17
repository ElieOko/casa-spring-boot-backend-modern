package server.web.casa.route.actor

import server.web.casa.route.GlobalRoute

object ActorRoute {
    const val MEMBER = "${GlobalRoute.ROOT}/${ActorFeatures.MEMBER_PATH}"
}

object ActorFeatures{
    const val MEMBER_PATH = "members"
}