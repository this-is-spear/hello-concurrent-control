package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import tis.hello_concurrent_control.domain.PointTransaction
import tis.hello_concurrent_control.repository.PointTransactionRepository

@Service
class TransactionService(
    private val pointTransactionRepository: PointTransactionRepository,
) {
    fun saveTransaction(pointTransaction: PointTransaction) {
        pointTransactionRepository.save(pointTransaction)
    }
}
