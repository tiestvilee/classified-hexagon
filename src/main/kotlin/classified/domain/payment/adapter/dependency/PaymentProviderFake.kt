package classified.domain.payment.adapter.dependency

import classified.domain.payment.model.Address
import classified.domain.payment.model.AuthorisationId
import classified.domain.payment.model.CardDetails
import classified.domain.payment.model.Money
import classified.domain.payment.port.dependency.PaymentProvider
import classified.domain.payment.port.service.PaymentHubError
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import kotlin.random.Random

class PaymentProviderFake : PaymentProvider {
    override fun authenticatePayment(
        billingAddress: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<AuthorisationId, PaymentHubError> {
        return Success(AuthorisationId.parse("asdf-" + Random.nextLong()))
    }
}