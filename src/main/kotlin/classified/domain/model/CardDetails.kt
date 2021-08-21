package classified.domain.model

enum class CardType {
    Amex, Visa, Mastercard
}

data class CardDetails(val cardType: CardType) {

}
