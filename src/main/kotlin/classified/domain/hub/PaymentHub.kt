package classified.domain.hub

import classified.domain.model.*
import classified.domain.port.plug.PaymentRepository
import classified.domain.port.socket.PaymentHubError
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

}
