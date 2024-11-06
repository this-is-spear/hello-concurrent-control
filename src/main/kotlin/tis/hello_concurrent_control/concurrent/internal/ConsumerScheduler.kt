package tis.hello_concurrent_control.concurrent.internal

import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class ConsumerScheduler(
    private val applicationContext: ApplicationContext,
) {
    @Scheduled(fixedDelay = 10)
    fun processMessages() {
        val beans = applicationContext.getBeansWithAnnotation(Service::class.java)
            .values
        beans.forEach { bean ->
            bean.javaClass.methods
                .first { it.name == "consumeTransactionRequest" }
                .apply {
                    invoke(bean)
                }
        }
    }
}
