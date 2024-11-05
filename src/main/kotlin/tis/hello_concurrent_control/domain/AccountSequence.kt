package tis.hello_concurrent_control.domain

import jakarta.persistence.Embeddable

@Embeddable
data class AccountSequence(
    val sequence: String,
)
