package classified.domain.model

import dev.forkhandles.values.IntValue
import dev.forkhandles.values.IntValueFactory

class ItemId(id: Int) : IntValue(id) {
    companion object : IntValueFactory<ItemId>(::ItemId)
}

enum class ItemState {
    Available, UnderOffer, Completed
}

data class Item(val name: String, val askingPrice: Money) {

}
