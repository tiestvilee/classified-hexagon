package classified.domain.payment.model

import dev.forkhandles.values.StringValue
import dev.forkhandles.values.StringValueFactory

class AuthorisationId(id: String) : StringValue(id) {
    companion object : StringValueFactory<AuthorisationId>(::AuthorisationId)
}