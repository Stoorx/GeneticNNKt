package model.fourier

import util.SingleRandom
import util.Visitor

class PitchedFourierSeries : FourierSeries {
    var pitch: Double = 0.0

    constructor():super()
    constructor(length: Int) : super(length)

    override fun accept(v: Visitor) {
        v.visit(this)
    }

    override fun calculate(x: Double, t: Double): Double {
        return super.calculate(x, t) + pitch * x
    }
}

fun PitchedFourierSeries.createFromRandom(length: Int): FourierSeries =
    PitchedFourierSeries(length).also {
        elements.forEach {
            it.amplitude = SingleRandom.nextDouble()
            it.phase = SingleRandom.nextDouble()
        }
        pitch = SingleRandom.nextDouble()
    }