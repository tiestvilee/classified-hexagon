package classified.domain.adapter

import classified.domain.model.*
import classified.domain.port.plug.PaymentRepository
import classified.domain.port.socket.PaymentHubError
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryPaymentRepository : PaymentRepository {

    private val payments = mutableMapOf<PaymentId, Payment>()

    override fun createPayment(
        offerId: OfferId,
        address: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<PaymentId, PaymentHubError> {
        val paymentId = PaymentId.random()
        payments[paymentId] = Payment(paymentId, PaymentState.Authorised, PaymentDetails(address, cardDetails, amount))
        return Success(paymentId)
    }

    override fun payment(paymentId: PaymentId): Result<Payment, PaymentHubError> {
        return payments[paymentId]?.let {
            Success(it)
        } ?: Failure(PaymentHubError.PaymentNotFound("with ad $paymentId"))
    }
}