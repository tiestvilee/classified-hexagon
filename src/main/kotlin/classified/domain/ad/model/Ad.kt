package classified.domain.model

import classified.domain.payment.model.Money
import dev.forkhandles.values.UUIDValue
import dev.forkhandles.values.UUIDValueFactory
import java.util.*

class AdId(id: UUID) : UUIDValue(id) {
    companion object : UUIDValueFactory<AdId>(::AdId)
}

enum class AdState {
    Available, UnderOffer, Completed
}

data class AdDetails(val name: String, val askingPrice: Money)

data class Ad(val id: AdId, val state: AdState, val details: AdDetails)