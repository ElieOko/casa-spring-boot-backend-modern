package server.web.casa.app.address.application.command

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.domain.model.request.QuartierRequest
import server.web.casa.app.address.infrastructure.persistence.entity.*
import server.web.casa.app.address.infrastructure.persistence.repository.*
import server.web.casa.app.property.domain.model.dto.PropertyMasterDTO
import server.web.casa.utils.KinshasaDistrict
import server.web.casa.utils.Mode
import kotlin.jvm.optionals.toList

@Component
@Order(3)
@Profile(Mode.DEV)
class CommandLineAddressComponent(
   private val cityRepository: CityRepository,
   private val countryRepository: CountryRepository,
   private val quartierRepository: QuartierRepository,
   private val districtRepository: DistrictRepository,
   private val communeRepository: CommuneRepository
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String) {
        log.info("********block***************")
        try {
       //     runBlocking {
//                createCountry()
//                createCity()
//                createDistrict()
//                createCommune()
//             //   createQuartier()
   //         }
//            createQuartier()
//            log.info("quartier")
//            log.info(quartierRepository.findAll()[0].commune?.name)
        } catch (e: Exception){
            log.info(e.message)
        }
    }

    suspend fun createCity(){
        //val country = countryRepository.findById(1).toList()
        cityRepository.save(CityEntity(
            id= null,
            countryId = 1,
            name ="Kinshasa",
        ))
        log.info("save city")
    }
    suspend fun createCountry(){
        countryRepository.save(
            CountryEntity(
                id = null,
                name = "République démocratique du Congo"
            )
        )
        log.info("save country")
    }
////
suspend fun createDistrict(){
        try {
//            val city = cityRepository.findById(1).orElse(null)
            val city : Long = 1
            val state = districtRepository.saveAll(
                    listOf(
                        DistrictEntity(
                            id = null,
                            cityId = 1,
                            name = KinshasaDistrict.FUNA
                        ),
                        DistrictEntity(
                            id = null,
                            cityId = city,
                            name = KinshasaDistrict.MONT_AMBA
                        ),
                        DistrictEntity(
                            cityId = city,
                            name = KinshasaDistrict.LUKUNGA
                        ),
                        DistrictEntity(
                            cityId = city,
                            name = KinshasaDistrict.TSHANGU
                        )
                    )
                ).toList()
                log.info("save district ${state.size}")
        } catch (e : Exception){TODO()

        }
    }
////
suspend fun createCommune(){
        val districtFuna = 1L
        val districtMontAmba = 2L
        val districtLukunga = 3L
        val districtTshangu = 4L
        val data = communeRepository.saveAll(
            listOf(
                CommuneEntity(
                    id = null,
                    districtId = districtFuna,
                    name = "Bandalungwa"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtLukunga,
                    name = "Barumbu"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtFuna,
                    name = "Bumbu"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtLukunga,
                    name = "Gombe"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtFuna,
                    name = "Kalamu"
                ),
                CommuneEntity(
                    districtId = districtFuna,
                    name = "Kasa-Vubu"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtTshangu,
                    name = "Kimbanseke"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtLukunga,
                    name = "Kinshasa"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtLukunga,
                    name = "Kintambo"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtMontAmba,
                    name = "Kisenso"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtMontAmba,
                    name = "Lemba"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtMontAmba,
                    name = "Limete"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtLukunga,
                    name = "Lingwala"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtFuna,
                    name = "Makala"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtTshangu,
                    name = "Maluku"
                ),
                CommuneEntity(
                    id = null,
                    districtId = districtTshangu,
                    name = "Masina"
                ),
                CommuneEntity(
                    districtId = districtMontAmba,
                    name = "Matete"
                ),
                CommuneEntity(
                    districtId = districtMontAmba,
                    name = "Mont-Ngafula"
                ),
                CommuneEntity(
                    districtId = districtTshangu,
                    name = "Ndjili"
                ),
                CommuneEntity(
                    districtId = districtMontAmba,
                    name = "Ngaba"
                ),
                CommuneEntity(
                    districtId = districtMontAmba,
                    name = "Ngaliema"
                ),
                CommuneEntity(
                    districtId = districtFuna,
                    name = "Ngiri-Ngiri"
                ),
                CommuneEntity(
                    districtId = districtTshangu,
                    name = "Nsele"
                ),
                CommuneEntity(
                    districtId = districtFuna,
                    name = "Selembao"
                )
            )
        ).toList()
        log.info("save commune ${data.size}")
    }
//
    suspend fun createQuartier(){
        log.info("in")
        val quartiers = listOf(
            QuartierRequest("TSHIBANGU", 1),
            QuartierRequest("MAKELELE", 1),
            QuartierRequest("KASA-VUBU", 1),
            QuartierRequest("BISENGO", 1),
            QuartierRequest("ADOULA", 1),
            QuartierRequest("LUBUNDI", 1),
            QuartierRequest("MOLAERT", 1),
            QuartierRequest("LUMUMBA", 1),

            // BARUMBU 2
            QuartierRequest("BITSHAKA", 2),
            QuartierRequest("FUNA I", 2),
            QuartierRequest("FUNA II", 2),
            QuartierRequest("KAPINGA BAPU", 2),
            QuartierRequest("KASAFI", 2),
            QuartierRequest("LIBULU", 2),
            QuartierRequest("MOZINDO", 2),
            QuartierRequest("N'DOLO", 2),
            QuartierRequest("TSHIMANGA", 2),

            // BUMBU 3
            QuartierRequest("DIPIYA", 3),
            QuartierRequest("MONGALA", 3),
            QuartierRequest("UBANGI", 3),
            QuartierRequest("LOKORO", 2),
            QuartierRequest("KASAI", 3),
            QuartierRequest("KWANGO", 3),
            QuartierRequest("LUKENIE", 3),
            QuartierRequest("MAI-NDOMBE", 3),
            QuartierRequest("MATADI", 3),
            QuartierRequest("LIEUTENANT MBAKI", 3),
            QuartierRequest("MBANDAKA", 3),
            QuartierRequest("MFIMI", 3),
            QuartierRequest("NTOMBA", 3),

            // GOMBE 4
            QuartierRequest("BATETELA", 4),
            QuartierRequest("HAUT COMMANDEMENT", 4),
            QuartierRequest("CROIX-ROUGE", 4),
            QuartierRequest("LEMERA", 4),
            QuartierRequest("GOLF", 4),
            QuartierRequest("FLEUVE", 4),
            QuartierRequest("COMMERCE", 4),
            QuartierRequest("GARE", 4),
            QuartierRequest("REVOLUTION", 4),
            QuartierRequest("CLINIQUE", 4),

            // KALAMU 5
            QuartierRequest("MATONGE I", 5),
            QuartierRequest("MATONGE II", 5),
            QuartierRequest("MATONGE III", 5),
            QuartierRequest("YOLO NORD I", 5),
            QuartierRequest("YOLO NORD II", 5),
            QuartierRequest("YOLO NORD III", 5),
            QuartierRequest("YOLO SUD I", 5),
            QuartierRequest("YOLO SUD II", 5),
            QuartierRequest("YOLO SUD III", 5),
            QuartierRequest("YOLO SUD IV", 5),
            QuartierRequest("KAUKA I", 5),
            QuartierRequest("KAUKA II", 5),
            QuartierRequest("KAUKA III", 5),
            QuartierRequest("PINZI", 5),
            QuartierRequest("IMMO CONGO", 5),

            // KASA-VUBU 6
            QuartierRequest("ANCIENS COMBATTANTS", 6),
            QuartierRequest("ASSOSSA", 6),
            QuartierRequest("KATANGA", 6),
            QuartierRequest("LODJA", 6),
            QuartierRequest("LUBUMBASHI", 6),
            QuartierRequest("ONC", 6),
            QuartierRequest("SALANGO", 6),
            // Kimbanseke 7
            QuartierRequest("17 MAI", 7),
            QuartierRequest("BAHUMBI", 7),
            QuartierRequest("BAMBOMA", 7),
            QuartierRequest("BIYELA", 7),
            QuartierRequest("BOMA", 7),
            QuartierRequest("DISSASI", 7),
            QuartierRequest("ESANGA", 7),
            QuartierRequest("KAMBA MULUMBA", 7),
            QuartierRequest("KASA-VUBU", 7),
            QuartierRequest("KIKIMI", 7),
            QuartierRequest("KISANGANI", 7),
            QuartierRequest("KISANTU IR BEN", 7),
            QuartierRequest("KUTU", 7),
            QuartierRequest("LUEBO", 7),
            QuartierRequest("MALONDA", 7),
            QuartierRequest("MANGANA", 7),
            QuartierRequest("MAVIOKELE", 7),
            QuartierRequest("MBUALA", 7),
            QuartierRequest("MFUMU NKENTO", 7),
            QuartierRequest("MIKONDO", 7),
            QuartierRequest("MOKALI", 7),
            QuartierRequest("MULIE", 7),
            QuartierRequest("NGAMAZIDA", 7),
            QuartierRequest("NGAMPANI", 7),
            QuartierRequest("NGANDU", 7),
            QuartierRequest("NGANGA", 7),
            QuartierRequest("NSUMABWA", 7),
            QuartierRequest("REVOLUTION", 7),
            QuartierRequest("SAKOMBI", 7),
            QuartierRequest("SALONGO", 7),

            // Kinshasa 8
            QuartierRequest("AKETI", 8),
            QuartierRequest("BOYOMA", 8),
            QuartierRequest("DJALO", 8),
            QuartierRequest("MADIMBA", 8),
            QuartierRequest("MONGALA", 8),
            QuartierRequest("NGBAKA", 8),
            QuartierRequest("PENDE", 8),

            // Kintambo 9
            QuartierRequest("ITIMBIRI", 9),
            QuartierRequest("KILIMANI", 9),
            QuartierRequest("LISALA", 9),
            QuartierRequest("LUBUDILUKA", 9),
            QuartierRequest("NGANDA", 9),
            QuartierRequest("SALONGO", 9),
            QuartierRequest("TSHINKELA", 9),
            QuartierRequest("WENZE", 9),

            // Kinseso 10
            QuartierRequest("REGIDESO", 10),
            QuartierRequest("KUMBU", 10),
            QuartierRequest("MBUKU", 10),
            QuartierRequest("AMBA", 10),
            QuartierRequest("MISSION MUJINGA", 10),
            QuartierRequest("LIBERATION", 10),
            QuartierRequest("17 MAI", 10),
            QuartierRequest("NSOLA", 10),
            QuartierRequest("BIKANGA", 10),

            // Lemba 11
            QuartierRequest("MADRANDELLE", 11),
            QuartierRequest("MASANO", 11),
            QuartierRequest("KIMPWANZA", 11),
            QuartierRequest("COMMERCIALE", 11),
            QuartierRequest("MOLO", 11),
            QuartierRequest("FOIRE", 11),
            QuartierRequest("DE L'ECOLE", 11),
            QuartierRequest("ECHANGEUR", 11),
            QuartierRequest("GOMBELI", 11),
            QuartierRequest("SALONGO", 11),
            QuartierRequest("LIVULU", 11),
            QuartierRequest("KEMI", 11),
            QuartierRequest("MBANZA-LEMBA", 11),

            // Limete 12
            QuartierRequest("AGRICOLE", 12),
            QuartierRequest("FUNA", 12),
            QuartierRequest("INDUSTRIEL", 12),
            QuartierRequest("KINGABWA", 12),
            QuartierRequest("MASIALA", 12),
            QuartierRequest("MAYULU", 12),
            QuartierRequest("MBAMU", 12),
            QuartierRequest("MOMBELE", 12),
            QuartierRequest("MOSOSO", 12),
            QuartierRequest("MOTEBA", 12),
            QuartierRequest("NDANU", 12),
            QuartierRequest("NZADI", 12),
            QuartierRequest("RESIDENTIEL", 12),
            QuartierRequest("SALONGO", 12),

            // Lingwala 13
            QuartierRequest("LUFUNGULA", 13),
            QuartierRequest("SINGA", 13),
            QuartierRequest("MOPEPE", 13),
            QuartierRequest("WENZE", 13),
            QuartierRequest("PLC", 13),
            QuartierRequest("30 JUIN", 13),
            QuartierRequest("LOKOLE", 13),
            QuartierRequest("PAKA-DJUMA", 13),
            QuartierRequest("VOIX DU PEUPLE", 13),
            QuartierRequest("NGUNDA-LOKOMBE", 13),

            // Makala 14
            QuartierRequest("BAGOTA", 14),
            QuartierRequest("BAHUMBU", 14),
            QuartierRequest("BOLIMA", 14),
            QuartierRequest("KABILA", 14),
            QuartierRequest("KISANTU", 14),
            QuartierRequest("KWANGO", 14),
            QuartierRequest("LEMBA VILLAGE", 14),
            QuartierRequest("MABULU", 14),

            // Maluku 15
            QuartierRequest("BU", 15),
            QuartierRequest("DUMI", 15),
            QuartierRequest("KIKIMI", 15),
            QuartierRequest("KIMPONGO", 15),
            QuartierRequest("KINGAKATI", 15),
            QuartierRequest("KINGONO", 15),
            QuartierRequest("KINZONO", 15),
            QuartierRequest("MBANKANA", 15),
            QuartierRequest("MALUKU", 15),
            QuartierRequest("MAI-DOMBE", 15),
            QuartierRequest("MANGE-NGENGE", 15),
            QuartierRequest("MENKAO", 15),
            QuartierRequest("MONACO", 15),
            QuartierRequest("MONGATA", 15),
            QuartierRequest("MWE", 15),
            QuartierRequest("NGAMA", 15),
            QuartierRequest("NGUMA", 15),
            QuartierRequest("YOSO", 15),
            QuartierRequest("YO", 15),

            // Masina 16
            QuartierRequest("ABBATOIR", 16),
            QuartierRequest("BOBA", 16),
            QuartierRequest("LONGO", 16),
            QuartierRequest("EFOLOKO", 16),
            QuartierRequest("KASAI", 16),
            QuartierRequest("KIMBANGU", 16),
            QuartierRequest("MAFUTA-KIZOLA", 16),
            QuartierRequest("MATADI", 16),
            QuartierRequest("MFUMU-SUKA", 16),
            QuartierRequest("NZUNZI WA MBOMBO", 16),
            QuartierRequest("PELENDE", 16),
            QuartierRequest("SANS-FIL", 16),
            QuartierRequest("TSHANGO", 16),
            QuartierRequest("TSHUENZE", 16),
            QuartierRequest("TELEVISION", 16),

            // Matete 17
            QuartierRequest("MANDINA", 17),
            QuartierRequest("MAI-NDOMBE", 17),
            QuartierRequest("MBOKOLO", 17),
            QuartierRequest("KWENGE I", 17),
            QuartierRequest("KWENGE II", 17),
            QuartierRequest("NGILIMA I", 17),
            QuartierRequest("NGILIMA II", 17),
            QuartierRequest("LOKORO", 17),
            QuartierRequest("ANUNGA", 17),
            QuartierRequest("NGUFU", 17),
            QuartierRequest("KINDA I", 17),
            QuartierRequest("KINDA II", 17),
            QuartierRequest("KUNDA I", 17),
            QuartierRequest("KUNDA II", 17),
            QuartierRequest("VIAZA", 17),
            QuartierRequest("BANUNU I", 17),
            QuartierRequest("BANUNU II", 17),
            QuartierRequest("BATANDU I", 17),
            QuartierRequest("BATANDU II", 17),
            QuartierRequest("DEBONHOMME", 17),
            QuartierRequest("PULULU I", 17),
            QuartierRequest("PULULU II", 17),
            QuartierRequest("BABOMA", 17),
            QuartierRequest("KINZAZI", 17),
            QuartierRequest("TOMBA", 17),
            QuartierRequest("MONGO", 17),
            QuartierRequest("VITAMINE I", 17),
            QuartierRequest("VITAMINE II", 17),
            QuartierRequest("BATENDE", 17),
            QuartierRequest("KINSATU", 17),
            QuartierRequest("MUTOTO", 17),
            QuartierRequest("MPUNDI", 17),
            QuartierRequest("BAHUMBU I", 17),
            QuartierRequest("BAHUMBU II", 17),
            QuartierRequest("MALANDI I", 17),
            QuartierRequest("MALANDI II", 17),
            QuartierRequest("LOKELE I", 17),
            QuartierRequest("LOKELE II", 17),
            QuartierRequest("BATEKE", 17),
            QuartierRequest("SINGA", 17),
            QuartierRequest("KINSIMBU", 17),
            // Mon-Ngafula18
            QuartierRequest("CPA MUSHIE", 18),
            QuartierRequest("KIMBONDO", 18),
            QuartierRequest("KIMBUTA", 18),
            QuartierRequest("KIMWENZO", 18),
            QuartierRequest("KIMBUALA", 18),
            QuartierRequest("KINDELE", 18),
            QuartierRequest("LUTUNDELE", 18),
            QuartierRequest("MAMA MOBUTU", 18),
            QuartierRequest("MAMA YEMO", 18),
            QuartierRequest("MASANGA MBILA", 18),
            QuartierRequest("MUSAGU-TELECOM", 18),
            QuartierRequest("MAZAMBA", 18),
            QuartierRequest("MATADI KIBALA", 18),
            QuartierRequest("MATADI MAYO", 18),
            QuartierRequest("MITENDI", 18),
            QuartierRequest("MBUKI", 18),
            QuartierRequest("NDJILI-KILAMBA", 18),
            QuartierRequest("NGANSELE", 18),
            QuartierRequest("PLATEAU I", 18),
            QuartierRequest("PLATEAU II", 18),
            QuartierRequest("VUNDA-MANENGA", 18),

            // N'DJILI19
            QuartierRequest("QUARTIER 1", 19),
            QuartierRequest("QUARTIER 2", 19),
            QuartierRequest("QUARTIER 3", 19),
            QuartierRequest("QUARTIER 4", 19),
            QuartierRequest("QUARTIER 5", 19),
            QuartierRequest("QUARTIER 6", 19),
            QuartierRequest("QUARTIER 7", 19),
            QuartierRequest("QUARTIER 8", 19),
            QuartierRequest("QUARTIER 9", 19),
            QuartierRequest("QUARTIER 10", 19),
            QuartierRequest("QUARTIER 11", 19),
            QuartierRequest("QUARTIER 12", 19),
            QuartierRequest("QUARTIER 13", 19),

            // Ngaba20
            QuartierRequest("MUKULUA", 20),
            QuartierRequest("BULA-MBEMBA", 20),
            QuartierRequest("MATEBA", 20),
            QuartierRequest("LUYI", 20),
            QuartierRequest("MPILA", 20),
            QuartierRequest("BAOBAB", 20),

            // Ngaliema21
            QuartierRequest("LUKUNGA", 21),
            QuartierRequest("NGOMBA KIKUSA", 21),
            QuartierRequest("BUMBA", 21),
            QuartierRequest("BINZA PIGEON DJELO BINZA", 21),
            QuartierRequest("BANGU", 21),
            QuartierRequest("PUNDA", 21),
            QuartierRequest("KIMPE", 21),
            QuartierRequest("ANCIENS COMBATTANTS", 21),
            QuartierRequest("BASOKO", 21),
            QuartierRequest("CONGO", 21),
            QuartierRequest("JOLI PARC", 21),
            QuartierRequest("KINKENDA", 21),
            QuartierRequest("KINSUKA PECHEUR", 21),
            QuartierRequest("LONZO", 21),
            QuartierRequest("MUSEYI", 21),
            QuartierRequest("MAMAN-YEMO", 21),
            QuartierRequest("MANENGA", 21),
            QuartierRequest("MFINDA", 21),
            QuartierRequest("MONGANYA", 21),
            QuartierRequest("LUBUDI", 21),

            // Ngiri-Ngiri22
            QuartierRequest("DIANGENDA", 22),
            QuartierRequest("24 NOVEMBRE", 22),
            QuartierRequest("SAIO", 22),
            QuartierRequest("PETIT-PETIT", 22),
            QuartierRequest("ASSOSSA", 22),
            QuartierRequest("DIOMI", 22),
            QuartierRequest("KARTHOUM", 22),
            QuartierRequest("ELENGESA", 22),

            // N'Sele23
            QuartierRequest("BADARA", 23),
            QuartierRequest("BIBWA", 23),
            QuartierRequest("D.I.C", 23),
            QuartierRequest("DINGI-DINGI", 23),
            QuartierRequest("DOMAINE", 23),
            QuartierRequest("MIKALA I", 23),
            QuartierRequest("MIKALA II", 23),
            QuartierRequest("KINKOLE", 23),
            QuartierRequest("PECHEURS", 23),
            QuartierRequest("BAHUMBU I", 23),
            QuartierRequest("BAHUMBU II", 23),
            QuartierRequest("SICOTRA/COKALI", 23),
            QuartierRequest("MIKONDO(BRASSERIE)", 23),
            QuartierRequest("MIKONGA(CITE DE MPASA)", 23),
            QuartierRequest("MAI-NDOMBE(VILLAGE)", 23),
            QuartierRequest("MPASSA MABA", 23),
            QuartierRequest("KINZONO", 23),
            QuartierRequest("MANGENGE", 23),
            QuartierRequest("MOBA NSE", 23),
            QuartierRequest("MENKAO(VILLAGE)", 23),
            QuartierRequest("MONACO", 23),
            QuartierRequest("NGIMA(VILLAGE)", 23),
            QuartierRequest("KINGAKATI(VILLAGE)", 23),
            QuartierRequest("TALANGAI", 23),

            // Selembao24
            QuartierRequest("BADIADING", 24),
            QuartierRequest("CITE VERTE", 24),
            QuartierRequest("INGA", 24),
            QuartierRequest("HERADY", 24),
            QuartierRequest("KALUNGA", 24),
            QuartierRequest("KINGU", 24),
            QuartierRequest("KONDE", 24),
            QuartierRequest("LIBERATION", 24),
            QuartierRequest("LUBUDI", 24),
            QuartierRequest("MADIATA", 24),
            QuartierRequest("MBOLA", 24),
            QuartierRequest("MOLENDE", 24),
            QuartierRequest("MUANA", 24),
            QuartierRequest("NTUNU", 24),
            QuartierRequest("NDOBE", 24),
            QuartierRequest("NGAFANI", 24),
            QuartierRequest("KOMBE", 24),
            QuartierRequest("NKULU", 24),
            QuartierRequest("PULULU MBAMBU", 24)
            //
        )
        log.info("in-step")
        val quartiersList = mutableListOf<QuartierEntity>()
        quartiers.forEach {
           // val commune = communeRepository.findById(it.communeId)
            quartiersList.add(QuartierEntity(id = null, communeId = it.communeId, name = it.name))
        }
        log.info("mutable")
        val add = quartierRepository.saveAll(quartiersList).toList()
        log.info("**********save quartier ${add.size}***********")
        log.info("out")

    }
}