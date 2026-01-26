package server.web.casa.app.actor.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import server.web.casa.app.actor.domain.model.Person

@Table(name = "persons")
class PersonEntity(
    @Id
    @Column("id")
    val id: Long? = null,   // AUTO-INCREMENT
    @Column("first_name")
    var firstName: String,
    @Column("last_name")
    var lastName: String,
    @Column("full_name")
    val fullName: String,
    @Column("address")
    var address: String? = "",
    @Column("images")
    var images: String? = null,
    @Column("card_front")
    var cardFront: String?,
    @Column("card_back")
    var cardBack: String? = null,
    @Column("number_card")
    val numberCard: String? = null,
    @Column("user_id")
    val userId: Long?,
    @Column("parrain_id")
    val parrainId: Long? = null,
    @Column("is_lock")
    var isLock: Boolean = false,
    @Column("type_card_id")
    val typeCardId: Long? = null
)

fun PersonEntity.toDomain() = Person(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    fullName = this.fullName,
    address = this.address,
    images = this.images,
    cardFront = this.cardFront,
    cardBack = this.cardBack,
    numberCard = this.numberCard,
    userId = this.userId,
    parrainId = this.parrainId,
    typeCardId = this.typeCardId,
)
    /*
    Ajoute deux champ : job => pour le service que user peut rendre
    (work, job=> chauffeur, plombier...) et
    acteurType => où on aura bailleur, commissionnaire et locateur, soit admin...
    en gros ces sont les memes info de preference on ajoute ces deux champs
    puis userId le différentie
    ou soit pour job on cree une table intermediaire
    au cas où un user aura plusieurs service rendre : user -userjob- job
    avec userjob (userId et jobId)
    si on doit ajouter des privileges specifiques on cree la table privilege
    où on va attribuer à user en fonction de acteurType
    */
