package classified.domain.adapter

import classified.domain.model.Item
import classified.domain.model.ItemDetails
import classified.domain.model.ItemId
import classified.domain.port.plug.AdRepository
import classified.domain.port.socket.AdHubError
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryAdRepository : AdRepository {
    val ads = mutableMapOf<ItemId, Item>()
    override fun insertAd(item: ItemDetails): Result<ItemId, AdHubError> {
        val itemId = ItemId.random()
        ads[itemId] = Item(itemId, item)
        return Success(itemId)
    }
}