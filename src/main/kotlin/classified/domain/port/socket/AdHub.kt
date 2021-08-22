package classified.domain.port.socket

import classified.domain.model.ItemDetails
import classified.domain.model.ItemId
import dev.forkhandles.result4k.Result

sealed class AdHubError(message: String) : Exception(message) {

}

interface AdHub {
    fun createAd(item: ItemDetails): Result<ItemId, AdHubError>
}