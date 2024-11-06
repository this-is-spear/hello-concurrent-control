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

@Aspect
@Component
@Order(-1)
class PointTransactionLockAspect(
    private val redissonClient: RedissonClient,
) {
    private val parser = SpelExpressionParser()

    @Around("@annotation(pointTransactionLock)")
    fun executeWithLock(joinPoint: ProceedingJoinPoint, pointTransactionLock: PointTransactionLock): Any? {
        check(pointTransactionLock.source != pointTransactionLock.target) { "source and target must be different" }
        val sourceKey = parseLockKey(pointTransactionLock.source, joinPoint)
        val targetKey = parseLockKey(pointTransactionLock.target, joinPoint)
        val keyPair = keyPair(sourceKey, targetKey)
        return lock(redissonClient.getLock(keyPair.first)) {
            lock(redissonClient.getLock(keyPair.second)) {
                joinPoint.proceed()
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
