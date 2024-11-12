package tis.hello_concurrent_control.transaction.request

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tis.hello_concurrent_control.transaction.reply.PublisherService

@Component
class ConsumerScheduler(
    private val consumerService: ConsumerService,
    private val publisherService: PublisherService,
) {
    @Scheduled(fixedDelay = 50)
    fun processMessages() {
        consumerService.consumeTransactionRequest()
            .map { publisherService.produce(it) }
            .subscribe()
    }
}
