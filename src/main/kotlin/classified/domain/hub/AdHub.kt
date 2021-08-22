package classified.domain.hub

import classified.domain.model.Ad
import classified.domain.model.AdDetails
import classified.domain.model.AdId
import classified.domain.port.plug.AdRepository
import classified.domain.port.socket.AdHubError
import dev.forkhandles.result4k.Result

class AdHub(private val repo: AdRepository) : classified.domain.port.socket.AdHub {
    override fun createAd(item: AdDetails): Result<AdId, AdHubError> {
        return repo.insertAd(item)
    }

    override fun findAdByName(adName: String): Result<Ad, AdHubError> {
        return repo.findAdByName(adName)
    }
}