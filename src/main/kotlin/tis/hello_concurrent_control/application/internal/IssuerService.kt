package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Issuer

@Service
class IssuerService {
    fun findIssuers(): List<Issuer> {
        return listOf(
            Issuer(AccountSequence("1234"))
        )
    }
}
