package classified.ad.hub

import classified.ad.port.plug.AdRepository
import classified.ad.port.socket.AdHub
import classified.ad.port.socket.AdHubError
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