package classified.domain.payment.port.dependency

import classified.domain.payment.model.Address
import classified.domain.payment.model.AuthorisationId
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.Money
import classified.domain.payment.port.service.PaymentHubError
import dev.forkhandles.result4k.Result

interface PaymentProvider {
    fun authenticatePayment(
        billingAddress: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<AuthorisationId, PaymentHubError>
}