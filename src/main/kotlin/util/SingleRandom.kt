package util

import java.util.*

object SingleRandom : Random() {
    override fun nextDouble(): Double {
        val factor = 10
        return 2 * factor * super.nextDouble() - (factor)
    }

    fun doubleZeroToOne(): Double = super.nextDouble()
}