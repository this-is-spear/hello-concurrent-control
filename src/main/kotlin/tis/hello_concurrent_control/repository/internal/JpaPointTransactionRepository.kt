package tis.hello_concurrent_control.repository.internal

import org.springframework.data.jpa.repository.JpaRepository
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointTransaction
import tis.hello_concurrent_control.repository.PointTransactionRepository

interface JpaPointTransactionRepository : JpaRepository<PointTransaction, Long>, PointTransactionRepository {
    override fun <S : PointTransaction?> save(entity: S & Any): S & Any
    override fun findBySourceAccountSequence(sourceAccountSequence: AccountSequence): List<PointTransaction>
    override fun findByTargetAccountSequence(targetAccountSequence: AccountSequence): List<PointTransaction>
}
