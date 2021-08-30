package test.classified.domain.ad.adapter

import classified.domain.ad.adapter.service.CliAdHubClient
import classified.domain.ad.adapter.service.CliAdHubServer
import classified.domain.ad.port.service.AdHub
import classified.domain.ad.port.service.AdHubError
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.payment.model.Money
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow
import org.junit.jupiter.api.Test


class CliAdHubTest {
    @Test
    fun `creates an Ad`() {
        val expectedAdDetails = AdDetails("this is the name", Money(45, 5))
        val expectedAdId = AdId.random()

        val server = CliAdHubServer(object : NotImplementedAdHub() {
            override fun createAd(item: AdDetails): Result<AdId, AdHubError> {
                assertThat(item, equalTo(expectedAdDetails))
                return Success(expectedAdId)
            }
        })
        val client = CliAdHubClient(server.process())

        val adId = client.createAd(expectedAdDetails).orThrow()

        assertThat(adId, equalTo(expectedAdId))
    }

    @Test
    fun `finds Ad with name`() {
        val expectedAdName = "search string"
        val expectedAd = Ad(AdId.random(), AdState.Available, AdDetails("ad name", Money(45, 5)))

        val server = CliAdHubServer(object : NotImplementedAdHub() {
            override fun findAdByName(adName: String): Result<Ad, AdHubError> {
                assertThat(adName, equalTo(expectedAdName))
                return Success(expectedAd)
            }
        })
        val client = CliAdHubClient(server.process())

        val adId = client.findAdByName(expectedAdName).orThrow()

        assertThat(adId, equalTo(expectedAd))
    }

    @Test
    fun `finds Ad by id`() {
        val expectedId = AdId.random()
        val expectedAd = Ad(expectedId, AdState.Available, AdDetails("ad name", Money(45, 5)))

        val server = CliAdHubServer(object : NotImplementedAdHub() {
            override fun ad(adId: AdId): Result<Ad, AdHubError> {
                assertThat(adId, equalTo(expectedId))
                return Success(expectedAd)
            }
        })
        val client = CliAdHubClient(server.process())

        val adId = client.ad(expectedId).orThrow()

        assertThat(adId, equalTo(expectedAd))
    }

    open class NotImplementedAdHub : AdHub {
        override fun createAd(item: AdDetails): Result<AdId, AdHubError> {
            TODO("Not yet implemented")
        }

        override fun findAdByName(adName: String): Result<Ad, AdHubError> {
            TODO("Not yet implemented")
        }

        override fun ad(adId: AdId): Result<Ad, AdHubError> {
            TODO("Not yet implemented")
        }
    }

}