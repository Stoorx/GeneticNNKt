package model.fourier

import util.Visitable
import util.Visitor

class FourierHypersurface() : Visitable {
    override fun accept(v: Visitor) {
        v.visit(this)
    }

    val dimensions: ArrayList<FourierSeries> = ArrayList()

    val arity: Int
        get() = dimensions.size

    constructor(length: Int, consumer: (Int) -> FourierSeries) : this() {
        dimensions.ensureCapacity(length)
        for (i in 0..length) {
            dimensions.add(consumer(i))
        }
    }

    fun calculate(x: List<Double>, t: Double): Double {
        assert(x.size == arity){
            "Arguments count and function arity is incompatible"
        }

        var result = 0.0
        dimensions.forEachIndexed { i, series ->
            result += series.calculate(x[i], t)
        }
        return result
    }
}

fun FourierHypersurface.createFromRandom(length: Int, consumer: (Int) -> FourierSeries): FourierHypersurface =
    FourierHypersurface(length, consumer)