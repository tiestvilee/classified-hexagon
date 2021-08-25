package classified.domain.payment.adapter

import classified.domain.model.OfferId
import classified.domain.payment.model.AuthorisationId
import classified.domain.payment.model.Payment
import classified.domain.payment.model.PaymentId
import classified.domain.payment.model.PaymentState
import classified.domain.payment.port.plug.PaymentRepository
import classified.domain.payment.port.socket.PaymentHubError
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class InMemoryPaymentRepository : PaymentRepository {

    private val payments = mutableMapOf<PaymentId, Payment>()

    override fun createPayment(
        offerId: OfferId,
        authorisationId: AuthorisationId,
    ): Result<PaymentId, PaymentHubError> {
        val paymentId = PaymentId.random()
        payments[paymentId] = Payment(paymentId, PaymentState.Authorised, authorisationId)
        return Success(paymentId)
    }

    override fun payment(paymentId: PaymentId): Result<Payment, PaymentHubError> {
        return payments[paymentId]?.let {
            Success(it)
        } ?: Failure(PaymentHubError.PaymentNotFound("with ad $paymentId"))
    }
}