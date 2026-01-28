package server.web.casa.route.actor

import server.web.casa.route.GlobalRoute

object MemberScope{
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${ActorFeatures.MEMBER_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${ActorFeatures.MEMBER_PATH}"
    const val PRIVATE ="${GlobalRoute.PRIVATE}/${ActorFeatures.MEMBER_PATH}"
}
object ActorFeatures{
    const val MEMBER_PATH = "members"
}