package classified.domain.ad.hub

import classified.domain.ad.port.dependency.AdRepository
import classified.domain.ad.port.service.AdHub
import classified.domain.ad.port.service.AdHubError
import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import dev.forkhandles.result4k.Result

class AdHub(private val repo: AdRepository) : AdHub {
    override fun createAd(item: AdDetails): Result<AdId, AdHubError> {
        return repo.insertAd(item)
    }

    override fun findAdByName(adName: String): Result<Ad, AdHubError> {
        return repo.findAdByName(adName)
    }

    override fun ad(adId: AdId): Result<Ad, AdHubError> {
        return repo.ad(adId)
    }
}