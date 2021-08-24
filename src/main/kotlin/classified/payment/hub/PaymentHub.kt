package classified.payment.hub

import classified.domain.model.*
import classified.domain.port.socket.PaymentHubError
import classified.payment.port.plug.PaymentRepository
import dev.forkhandles.result4k.Result

class PaymentHub(private val repository: PaymentRepository) : classified.domain.port.socket.PaymentHub {
    override fun createPayment(
        offerId: OfferId,
        address: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<PaymentId, PaymentHubError> {
        return repository.createPayment(offerId, address, cardDetails, amount)
    }

    override fun payment(paymentId: PaymentId): Result<Payment, PaymentHubError> {
        return repository.payment(paymentId)
    }

    override fun settle(paymentId: PaymentId): Result<Unit, PaymentHubError> {
        TODO("Not yet implemented")
    }

}
