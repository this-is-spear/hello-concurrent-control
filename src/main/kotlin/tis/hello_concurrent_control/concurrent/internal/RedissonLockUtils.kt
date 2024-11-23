package tis.hello_concurrent_control.concurrent.internal

import org.redisson.api.RLock
import java.util.concurrent.TimeUnit

fun keyPair(key1: String, key2: String) = if (key1 < key2) {
    Pair(key1, key2)
} else {
    Pair(key2, key1)
}

fun <T> lock(key: RLock, function: () -> T) = try {
    val lock = key.tryLock(30L, 30L, TimeUnit.SECONDS)
    check(lock) { "lock failed" }
    function()
} finally {
    if (key.isHeldByCurrentThread) {
        key.unlock()
    }
}
