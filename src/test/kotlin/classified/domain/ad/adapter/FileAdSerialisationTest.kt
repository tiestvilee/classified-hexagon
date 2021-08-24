package classified.domain.ad.adapter

import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.model.AdState
import classified.domain.payment.model.Money
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class FileAdSerialisationTest {
    @Test
    fun `can serialise and deserialise a bunch of Ads`() {
        val originalAds = listOf(
            Ad(AdId.random(), AdState.Available, AdDetails("random name", Money(34, 78))),
            Ad(AdId.random(), AdState.Completed, AdDetails("random name 2", Money(56, 12))),
        )
        val roundTripAds = deserialiseAds(serialiseAds(originalAds))
        assertThat(roundTripAds, equalTo(originalAds))
    }
}