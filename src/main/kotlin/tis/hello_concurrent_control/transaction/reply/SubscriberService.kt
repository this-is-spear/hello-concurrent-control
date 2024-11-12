package tis.hello_concurrent_control.transaction.reply

import java.time.Duration
import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.transaction.PointTransactionId

@Service
class SubscriberService(
    private val redissonReactiveClient: RedissonReactiveClient,
) {
    fun subscribe(requestId: PointTransactionId): Mono<PointResponseMessage> {
        val topic = redissonReactiveClient.getTopic(TOPIC_PREFIX + requestId.id)
        return topic.getMessages(PointResponseMessage::class.java)
            .next()
            .timeout(Duration.ofSeconds(5L))
    }
}
