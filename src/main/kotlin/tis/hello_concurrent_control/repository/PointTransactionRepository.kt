package tis.hello_concurrent_control.repository

import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.PointTransaction

interface PointTransactionRepository {
    fun <S : PointTransaction?> save(entity: S & Any): S & Any
    fun findBySourceAccountSequence(sourceAccountSequence: AccountSequence): List<PointTransaction>
    fun findByTargetAccountSequence(targetAccountSequence: AccountSequence): List<PointTransaction>
}
