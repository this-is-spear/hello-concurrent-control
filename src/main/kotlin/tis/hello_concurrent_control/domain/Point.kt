package tis.hello_concurrent_control.domain

import jakarta.persistence.Embeddable

@Embeddable
data class Point(
    val amount: Int = 0,
) {
    operator fun plus(o: Point): Point = Point(this.amount + o.amount)
    operator fun minus(o: Point): Point = Point(this.amount - o.amount)
    operator fun compareTo(amount: Point) = this.amount.compareTo(amount.amount)
}
