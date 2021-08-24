package classified.domain.payment.model

import dev.forkhandles.values.UUIDValue
import dev.forkhandles.values.UUIDValueFactory
import java.util.*

class PaymentId(id: UUID) : UUIDValue(id) {
    companion object : UUIDValueFactory<PaymentId>(::PaymentId)
}

enum class PaymentState {
    Authorised, Settled
}

data class PaymentDetails(
    val shippingAddress: Address,
    val cardDetails: CardDetails,
    val amount: Money
)

data class Payment(val id: PaymentId, val state: PaymentState, val details: PaymentDetails)