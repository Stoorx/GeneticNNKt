package model.fourier

import util.Visitable
import util.Visitor
import kotlin.math.PI
import kotlin.math.cos

data class FourierElement(
    var amplitude: Double = 0.0,
    var phase: Double = 0.0
) : Visitable {
    fun calculate(x: Double, k: Int, t: Double): Double =
        amplitude * cos(
            (2.0 * k * PI * x) /
                    (t.takeIf { it != 0.0 } ?: throw Exception("FE division by zero")) + phase)

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}