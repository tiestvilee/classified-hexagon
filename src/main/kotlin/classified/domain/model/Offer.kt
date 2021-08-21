package classified.domain.model

import dev.forkhandles.values.IntValue
import dev.forkhandles.values.IntValueFactory

class OfferId(offerId: Int) : IntValue(offerId) {
    companion object : IntValueFactory<OfferId>(::OfferId)
}

enum class OfferState {
    Offered, Accepted, OutForDelivery, Completed
}

data class Offer(val itemId: ItemId, val offer: Money) {
}