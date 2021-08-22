package classified.domain.port.plug

import classified.domain.model.ItemDetails
import classified.domain.model.ItemId
import classified.domain.port.socket.AdHubError
import dev.forkhandles.result4k.Result

interface AdRepository {
    fun insertAd(item: ItemDetails): Result<ItemId, AdHubError>

}
