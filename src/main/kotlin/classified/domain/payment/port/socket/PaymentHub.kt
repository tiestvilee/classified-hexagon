package classified.domain.payment.port.socket

import classified.domain.model.OfferId
import classified.domain.payment.model.*
import dev.forkhandles.result4k.Result

sealed class PaymentHubError(message: String) : Exception(message) {
    class PaymentNotFound(message: String) : PaymentHubError(message)
}

interface PaymentHub {
    fun createPayment(
        offerId: OfferId,
        address: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<PaymentId, PaymentHubError>

    fun payment(paymentId: PaymentId): Result<Payment, PaymentHubError>
    fun settle(paymentId: PaymentId): Result<Unit, PaymentHubError>
}