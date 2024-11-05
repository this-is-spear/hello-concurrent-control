package tis.hello_concurrent_control.concurrent

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PointTransactionLock(
    val source: String,
    val target: String,
)
