package classified.payment.hub

import classified.domain.model.OfferId
import classified.payment.model.*
import classified.payment.port.plug.PaymentRepository
import classified.payment.port.socket.PaymentHub
import classified.payment.port.socket.PaymentHubError
import dev.forkhandles.result4k.Result

class PaymentHub(private val repository: PaymentRepository) : PaymentHub {
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
