package tis.hello_concurrent_control

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloConcurrentControlApplication

fun main(args: Array<String>) {
    runApplication<HelloConcurrentControlApplication>(*args)
}
