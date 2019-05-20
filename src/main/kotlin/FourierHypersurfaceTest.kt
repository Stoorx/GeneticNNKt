import model.fourier.FourierHypersurface
import model.fourier.PitchedFourierSeries

fun main() {
    val fhs = FourierHypersurface(1) {
        PitchedFourierSeries.createFromRandom(3)
    }

    val start = -10.0
    val stop = 10.0
    val step = 0.1

    var curr = start

    val sb = StringBuilder()
    while (curr <= stop) {
        sb.appendln(
            "$curr, ${fhs.calculate(
                arrayListOf(curr),
                10.0
            )}"
        )
        curr += step
    }

    print(sb)
}