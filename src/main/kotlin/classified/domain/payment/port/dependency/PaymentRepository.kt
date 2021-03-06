package classified.domain.payment.port.dependency

import classified.domain.model.OfferId
import classified.domain.payment.model.AuthorisationId
import classified.domain.payment.model.Payment
import classified.domain.payment.model.PaymentId
import classified.domain.payment.port.service.PaymentHubError
import dev.forkhandles.result4k.Result

interface PaymentRepository {
    fun createPayment(
        offerId: OfferId,
        authorisationId: AuthorisationId,
    ): Result<PaymentId, PaymentHubError>

    fun payment(paymentId: PaymentId): Result<Payment, PaymentHubError>
}