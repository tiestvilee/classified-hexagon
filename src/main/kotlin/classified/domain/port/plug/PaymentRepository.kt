package classified.domain.port.plug

import classified.domain.model.*
import classified.domain.port.socket.PaymentHubError
import dev.forkhandles.result4k.Result

interface PaymentRepository {
    fun createPayment(
        offerId: OfferId,
        address: Address,
        cardDetails: CardDetails,
        amount: Money
    ): Result<PaymentId, PaymentHubError>
}