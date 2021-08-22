package classified.domain.model

import dev.forkhandles.values.UUIDValue
import dev.forkhandles.values.UUIDValueFactory
import java.util.*

class OfferId(offerId: UUID) : UUIDValue(offerId) {
    companion object : UUIDValueFactory<OfferId>(::OfferId)
}

enum class OfferState {
    Offered, Accepted, OutForDelivery, Completed
}

data class OfferDetails(val adId: AdId, val offer: Money) {
}

data class Offer(val id: OfferId, val details: OfferDetails)