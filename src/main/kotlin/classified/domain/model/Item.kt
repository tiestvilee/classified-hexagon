package classified.domain.model

import dev.forkhandles.values.UUIDValue
import dev.forkhandles.values.UUIDValueFactory
import java.util.*

class ItemId(id: UUID) : UUIDValue(id) {
    companion object : UUIDValueFactory<ItemId>(::ItemId)
}

enum class ItemState {
    Available, UnderOffer, Completed
}

data class ItemDetails(val name: String, val askingPrice: Money)

data class Item(val id: ItemId, val details: ItemDetails)