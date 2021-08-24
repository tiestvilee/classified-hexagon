package classified.payment.model

import java.math.BigDecimal

data class Money(val amount: BigDecimal) {
    constructor(dollars: Int, cents: Int = 0) : this(BigDecimal.valueOf(dollars * 100L + cents, 2))

    override fun toString() = "$$amount"
}
