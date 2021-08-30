package classified.domain.payment.adapter.service

import classified.domain.ad.adapter.service.CliAdParserError
import classified.domain.offer.adapter.service.toOfferId
import classified.domain.payment.model.*
import classified.domain.payment.port.service.PaymentHub
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.orThrow
import java.math.BigDecimal

class PaymentCliParserError(message: String) : Exception(message)

fun paymentCliProcessor(
    command: String,
    params: List<String>,
    domain: String,
    paymentHub: PaymentHub
) = when (command) {
    "create" -> {
        val paymentId = paymentHub.createPayment(
            params[0].toOfferId().orThrow(),
            Address(params.drop(1).dropLast(2).joinToString(" ")),
            CardDetails(CardType.valueOf(params[params.size - 2])),
            Money(BigDecimal(params.last()))
        ).orThrow()
        paymentId.toCliString()
    }
    else -> {
        throw PaymentCliParserError("Don't understand command $command in domain $domain")
    }
}

fun PaymentId.toCliString() = value.toString()

fun String.toPaymentId(): Result<PaymentId, CliAdParserError> {
    return Success(PaymentId.parse(this))
}

fun String.toPayment(): Result<Payment, CliAdParserError> {
    TODO("Not yet implemented")
}
