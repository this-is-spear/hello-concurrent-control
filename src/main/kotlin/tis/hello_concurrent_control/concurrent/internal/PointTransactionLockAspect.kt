package tis.hello_concurrent_control.concurrent.internal

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.core.annotation.Order
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import tis.hello_concurrent_control.concurrent.PointTransactionLock
import java.util.concurrent.TimeUnit

@Aspect
@Component
@Order(-1)
class PointTransactionLockAspect(
    private val redissonClient: RedissonClient,
) {
    private val parser = SpelExpressionParser()

    @Around("@annotation(pointTransactionLock)")
    fun executeWithLock(joinPoint: ProceedingJoinPoint, pointTransactionLock: PointTransactionLock): Any? {
        require(pointTransactionLock.source != pointTransactionLock.target) { "source and target must be different" }
        val sourceKey = parseLockKey(pointTransactionLock.source, joinPoint)
        val targetKey = parseLockKey(pointTransactionLock.target, joinPoint)

        val firstKey by lazy {
            if (sourceKey < targetKey) {
                sourceKey
            } else {
                targetKey
            }
        }

        val secondKey by lazy {
            if (sourceKey < targetKey) {
                targetKey
            } else {
                sourceKey
            }
        }

        val firstLock = redissonClient.getLock(firstKey)
        return try {
            val firstAcquired = firstLock.tryLock(3L, 3L, TimeUnit.SECONDS)
            if (!firstAcquired) {
                throw IllegalStateException()
            }

            val secondLock = redissonClient.getLock(secondKey)
            try {
                val secondAcquired = secondLock.tryLock(3L, 3L, TimeUnit.SECONDS)
                if (!secondAcquired) {
                    throw IllegalStateException()
                }

                joinPoint.proceed()
            } finally {
                if (secondLock.isHeldByCurrentThread) {
                    secondLock.unlock()
                }
            }
        } finally {
            if (firstLock.isHeldByCurrentThread) {
                firstLock.unlock()
            }
        }
    }

    private fun parseLockKey(keyExpression: String, joinPoint: ProceedingJoinPoint): String {
        val context = StandardEvaluationContext().apply {
            val signature = joinPoint.signature as MethodSignature
            val parameterNames = signature.parameterNames
            val args = joinPoint.args

            parameterNames.zip(args).forEach { (name, value) ->
                setVariable(name, value)
            }
        }

        return parser.parseExpression(keyExpression).getValue(context, String::class.java)
            ?: throw IllegalArgumentException("key expression is invalid")
    }
}
