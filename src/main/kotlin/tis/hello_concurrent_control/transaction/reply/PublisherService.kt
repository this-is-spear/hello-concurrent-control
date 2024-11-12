package tis.hello_concurrent_control.transaction.reply

import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Service

const val TOPIC_PREFIX = "point-response"

@Service
class PublisherService(
    private val redissonReactiveClient: RedissonReactiveClient,
) {
    fun produce(pointResponseMessage: PointResponseMessage) {
        val topic = redissonReactiveClient.getTopic(TOPIC_PREFIX + pointResponseMessage.pointTransactionId.id)
        topic.publish(pointResponseMessage)
            .subscribe()
    }
}
