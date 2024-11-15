package tis.hello_concurrent_control.application.internal

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import tis.hello_concurrent_control.config.ShardedMessageService
import tis.hello_concurrent_control.domain.AccountSequence
import tis.hello_concurrent_control.domain.Point

@Service
class SendAndReplyService(
    private val shardedMessageService: ShardedMessageService,
) {
    fun transaction(
        sourceAccount: AccountSequence,
        targetAccount: AccountSequence,
        amount: Point,
    ): Mono<PointResponseMessage> {
        val requestMessage = PointRequestMessage(sourceAccount, targetAccount, amount)
        return Mono.fromFuture {
            shardedMessageService.sendWithConsistentHashing(targetAccount.toString(), requestMessage)
        }
    }
}
