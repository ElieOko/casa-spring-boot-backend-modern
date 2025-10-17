package server.web.casa.app.address.application.command

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import server.web.casa.app.address.infrastructure.persistence.entity.CityEntity
import server.web.casa.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.casa.app.address.infrastructure.persistence.entity.CountryEntity
import server.web.casa.app.address.infrastructure.persistence.entity.DistrictEntity
import server.web.casa.app.address.infrastructure.persistence.repository.CityRepository
import server.web.casa.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.casa.app.address.infrastructure.persistence.repository.CountryRepository
import server.web.casa.app.address.infrastructure.persistence.repository.DistrictRepository
import server.web.casa.app.address.infrastructure.persistence.repository.QuartierRepository
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
        log.info("***********************")
        try {
            createCountry()
            createCity()
            createDistrict()
            createCommune()
        } catch (e: Exception){
            log.info(e.message)
        }

    }

    fun createCity(){
        val country = countryRepository.findById(1).toList()
        cityRepository.save(CityEntity(
            cityId = 0,
            country = country[0],
            name ="Kinshasa",
        ))
        log.info("save city")
    }
    fun createCountry(){
        countryRepository.save(
            CountryEntity(
                countryId = 0,
                name = "République démocratique du Congo"
            )
        )
        log.info("save country")
    }

    fun createDistrict(){
        try {
            val city = cityRepository.findById(1).orElse(null)
            if (city != null){
                districtRepository.saveAll(
                    listOf(
                        DistrictEntity(
                            city = city,
                            name = KinshasaDistrict.FUNA
                        ),
                        DistrictEntity(
                            city = city,
                            name = KinshasaDistrict.MONT_AMBA
                        ),
                        DistrictEntity(
                            city = city,
                            name = KinshasaDistrict.LUKUNGA
                        ),
                        DistrictEntity(
                            city = city,
                            name = KinshasaDistrict.TSHANGU
                        )
                    )
                )
                log.info("save district")
            }
        } catch (e : Exception){TODO()

        }
    }

    fun createCommune(){
        val districtFuna = districtRepository.findById(1).orElse(null)
        val districtMontAmba = districtRepository.findById(2).orElse(null)
        val districtLukunga = districtRepository.findById(3).orElse(null)
        val districtTshangu = districtRepository.findById(4).orElse(null)

        communeRepository.saveAll(
            listOf(
                CommuneEntity(
                    district = districtFuna,
                    name = "Bandalungwa"
                ),
                CommuneEntity(
                    district = districtLukunga,
                    name = "Barumbu"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Bumbu"
                ),
                CommuneEntity(
                    district = districtLukunga,
                    name = "Gombe"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Kalamu"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Kasa-Vubu"
                ),
                CommuneEntity(
                    district = districtTshangu,
                    name = "Kimbanseke"
                ),
                CommuneEntity(
                    district = districtLukunga,
                    name = "Kinshasa"
                ),
                CommuneEntity(
                    district = districtLukunga,
                    name = "Kintambo"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Kisenso"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Lemba"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Limete"
                ),
                CommuneEntity(
                    district = districtLukunga,
                    name = "Lingwala"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Makala"
                ),
                CommuneEntity(
                    district = districtTshangu,
                    name = "Maluku"
                ),
                CommuneEntity(
                    district = districtTshangu,
                    name = "Masina"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Matete"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Mont-Ngafula"
                ),
                CommuneEntity(
                    district = districtTshangu,
                    name = "Ndjili"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Ngaba"
                ),
                CommuneEntity(
                    district = districtMontAmba,
                    name = "Ngaliema"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Ngiri-Ngiri"
                ),
                CommuneEntity(
                    district = districtTshangu,
                    name = "Nsele"
                ),
                CommuneEntity(
                    district = districtFuna,
                    name = "Selembao"
                )
            )
        )
        log.info("save commune")
    }

    fun createQuartier(){

    }
}