package classified.domain.hub

import classified.domain.model.ItemDetails
import classified.domain.model.ItemId
import classified.domain.port.plug.AdRepository
import classified.domain.port.socket.AdHubError
import dev.forkhandles.result4k.Result

class AdHub(private val repo: AdRepository) : classified.domain.port.socket.AdHub {
    override fun createAd(item: ItemDetails): Result<ItemId, AdHubError> {
        return repo.insertAd(item)
    }
}