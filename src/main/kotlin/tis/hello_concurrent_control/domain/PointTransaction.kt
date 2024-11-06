package tis.hello_concurrent_control.domain

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class PointTransaction(
    @Embedded
    @AttributeOverride(name = "sequence", column = Column(name = "source_account_sequence"))
    val sourceAccountSequence: AccountSequence,
    @Embedded
    @AttributeOverride(name = "sequence", column = Column(name = "target_account_sequence"))
    val targetAccountSequence: AccountSequence,
    @Embedded
    val amount: Point,
    val createdAt: Long = System.currentTimeMillis(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
