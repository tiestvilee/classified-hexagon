package classified.payment.model

enum class CardType {
    Amex, Visa, Mastercard
}

data class CardDetails(val cardType: CardType) {

}
