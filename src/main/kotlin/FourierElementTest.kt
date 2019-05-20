import model.fourier.FourierElement
import util.SingleRandom
import java.io.FileOutputStream

fun main() {
    val fe = FourierElement(SingleRandom.nextDouble(), SingleRandom.nextDouble())

    val start = -10.0
    val stop = 10.0
    val step = 0.1

    var curr = start

    val sb = StringBuilder()
    while (curr <= stop) {
        sb.appendln(
            "$curr, ${fe.calculate(
                curr, 1, 10.0
            )}"
        )
        curr += step
    }

    print(sb)
    val out = FileOutputStream("FETest.txt")
    out.write(sb.toString().toByteArray())
}