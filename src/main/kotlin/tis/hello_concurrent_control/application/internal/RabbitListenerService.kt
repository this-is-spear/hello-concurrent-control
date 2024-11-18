package tis.hello_concurrent_control.application.internal

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import tis.hello_concurrent_control.config.QUEUE_1
import tis.hello_concurrent_control.config.QUEUE_2
import tis.hello_concurrent_control.config.QUEUE_3

@Service
class RabbitListenerService(
    private val receiveAndRequestService: ReceiveAndRequestService,
) {
    @RabbitListener(queues = [QUEUE_1])
    fun subscribe1(pointRequestMessage: PointRequestMessage): PointResponseMessage {
        return receiveAndRequestService.subscribe(pointRequestMessage)
    }

    @RabbitListener(queues = [QUEUE_2])
    fun subscribe2(pointRequestMessage: PointRequestMessage): PointResponseMessage {
        return receiveAndRequestService.subscribe(pointRequestMessage)
    }

    @RabbitListener(queues = [QUEUE_3])
    fun subscribe3(pointRequestMessage: PointRequestMessage): PointResponseMessage {
        return receiveAndRequestService.subscribe(pointRequestMessage)
    }
}
