package classified.domain.model

import dev.forkhandles.values.IntValue
import dev.forkhandles.values.IntValueFactory

class PaymentId(id: Int) : IntValue(id) {
    companion object : IntValueFactory<PaymentId>(::PaymentId)
}

enum class PaymentState {
    Authorised, Settled
}

data class PaymentDetails(
    val offerId: OfferId,
    val shippingAddress: Address,
    val cardDetails: CardDetails,
    val amount: Money
) {
}