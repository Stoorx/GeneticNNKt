package model.fourier

import util.SingleRandom

open class FourierSeries() {
    val elements: ArrayList<FourierElement> = ArrayList()
    val length: Int
        get() = elements.size

    constructor(length: Int) : this() {
        elements.ensureCapacity(length)
        for (i in 0..length) {
            elements.add(FourierElement())
        }
    }

    open fun calculate(x: Double, t: Double): Double {
        var result: Double = 0.0
        elements.forEachIndexed { i, element ->
            result += element.calculate(x, i, t)
        }
        return result
    }
}

fun FourierSeries.createFromRandom(length: Int): FourierSeries =
    FourierSeries(length).also {
        elements.forEach {
            it.amplitude = SingleRandom.nextDouble()
            it.phase = SingleRandom.nextDouble()
        }
    }
