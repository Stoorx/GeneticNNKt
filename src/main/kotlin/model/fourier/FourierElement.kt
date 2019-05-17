package model.fourier

import kotlin.math.PI
import kotlin.math.cos

data class FourierElement(
    var amplitude: Double = 0.0,
    var phase: Double = 0.0
) {
    fun calculate(x: Double, k: Int, t: Double): Double =
        amplitude * cos((2.0 * k * PI * x) / t + phase)
}