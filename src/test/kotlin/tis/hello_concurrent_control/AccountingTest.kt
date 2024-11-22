package tis.hello_concurrent_control

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class AccountingTest {
    data class Transaction(
        val source: String,
        val target: String,
        val amount: Long,
        val createdAt: LocalDateTime
    )

    data class PointSource(
        val institutionId: String,
        val amount: Long
    )

    data class PointUsageWithSources(
        val transaction: Transaction,
        val sources: List<PointSource>
    )

    class PointTracker {
        private val pointSourceMap = mutableMapOf<String, MutableList<PointSource>>()

        fun trackPoints(transactions: List<Transaction>, targetDate: LocalDate): Map<String, List<PointUsageWithSources>> {
            pointSourceMap.clear() // 초기화

            // 시간순으로 정렬하여 처리
            val sortedTransactions = transactions.sortedBy { it.createdAt }
            sortedTransactions.forEach { processTransaction(it) }

            // 특정 날짜의 기관 사용 내역만 필터링
            return sortedTransactions
                .filter {
                    it.createdAt.toLocalDate() == targetDate &&
                            isInstitution(it.target)
                }
                .groupBy(
                    keySelector = { it.target },
                    valueTransform = { transaction ->
                        val sources = getSourcesForTransaction(transaction)
                        PointUsageWithSources(transaction, sources)
                    }
                )
        }

        private fun processTransaction(transaction: Transaction) {
            if (isInstitution(transaction.source)) {
                // 기관에서 발급하는 경우
                val targetPoints = pointSourceMap.getOrPut(transaction.target) { mutableListOf() }
                targetPoints.add(PointSource(transaction.source, transaction.amount))
            } else {
                // 포인트 사용/이체하는 경우
                val sourcePoints = pointSourceMap[transaction.source] ?: return
                val remainingPoints = sourcePoints.toMutableList()
                val targetPoints = pointSourceMap.getOrPut(transaction.target) { mutableListOf() }

                var remainingAmount = transaction.amount
                val usedSources = mutableListOf<PointSource>()

                while (remainingAmount > 0 && remainingPoints.isNotEmpty()) {
                    val currentPoint = remainingPoints.removeAt(0)

                    if (currentPoint.amount <= remainingAmount) {
                        // 포인트를 전부 사용
                        usedSources.add(currentPoint)
                        targetPoints.add(currentPoint)
                        remainingAmount -= currentPoint.amount
                    } else {
                        // 포인트를 일부만 사용
                        val usedAmount = remainingAmount
                        val remainingPointAmount = currentPoint.amount - remainingAmount

                        usedSources.add(PointSource(currentPoint.institutionId, usedAmount))
                        targetPoints.add(PointSource(currentPoint.institutionId, usedAmount))
                        remainingPoints.add(0, PointSource(currentPoint.institutionId, remainingPointAmount))
                        remainingAmount = 0
                    }
                }

                // 원본 소스 포인트 맵 업데이트
                pointSourceMap[transaction.source] = remainingPoints
            }
        }

        private fun getSourcesForTransaction(transaction: Transaction): List<PointSource> {
            val sourcePoints = pointSourceMap[transaction.source] ?: return emptyList()
            var remainingAmount = transaction.amount
            val result = mutableListOf<PointSource>()
            var sourceIndex = 0

            while (remainingAmount > 0 && sourceIndex < sourcePoints.size) {
                val point = sourcePoints[sourceIndex]
                if (point.amount <= remainingAmount) {
                    result.add(point)
                    remainingAmount -= point.amount
                } else {
                    result.add(PointSource(point.institutionId, remainingAmount))
                    remainingAmount = 0
                }
                sourceIndex++
            }

            return result
        }

        private fun isInstitution(id: String): Boolean {
            return id.startsWith("INST_")
        }
    }

    @Test
    fun main() {
        val tracker = PointTracker()
        val today = LocalDate.now()

        val transactions = listOf(
            Transaction("INST_A", "USER_1", 200, LocalDateTime.now().minusDays(1)),
            Transaction("INST_B", "USER_1", 200, LocalDateTime.now().minusDays(1)),
            Transaction("USER_1", "INST_C", 800, LocalDateTime.now()),
            Transaction("USER_1", "USER_2", 400, LocalDateTime.now()),
            Transaction("USER_2", "INST_D", 300, LocalDateTime.now())
        )

        val result = tracker.trackPoints(transactions, today)

        result.forEach { (institution, usages) ->
            println("Institution: $institution")
            usages.forEach { usage ->
                println("  Transaction amount: ${usage.transaction.amount}")
                println("  Sources:")
                usage.sources.forEach { source ->
                    println("    From ${source.institutionId}: ${source.amount}")
                }
            }
        }
    }
}
