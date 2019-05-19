package model

import util.SingleRandom


data class DatasetElement(
    val inputs: DoubleArray,
    val output: Double = 0.0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatasetElement

        if (!inputs.contentEquals(other.inputs)) return false
        if (output != other.output) return false

        return true
    }

    override fun hashCode(): Int {
        var result = inputs.contentHashCode()
        result = 31 * result + output.hashCode()
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder()
        inputs.forEach {
            sb.append("%.9f".format(it).replace(',', '.') + ", ")
        }
        return sb.toString() + "%.9f".format(output).replace(',', '.')
    }
}

class Dataset {
    val dataPoints: ArrayList<DatasetElement> = ArrayList()
    var function: ((DoubleArray) -> Double)? = null
    override fun toString(): String {
        val sb = StringBuilder()
        dataPoints.forEach {
            sb.append("$it\n")
        }
        return sb.toString()
    }

    companion object {
        fun generateFromFunction(
            lowerBounds: DoubleArray,
            upperBounds: DoubleArray,
            count: Int,
            function: (DoubleArray) -> Double
        ): Dataset {
            if (lowerBounds.size != upperBounds.size)
                throw Exception("Incompatible bounds sizes")

            val ds = Dataset()

            for (i in 0..count) {
                val inputVector = DoubleArray(lowerBounds.size) {
                    val randomDouble = SingleRandom.doubleZeroToOne()
                    (upperBounds[it] - lowerBounds[it]) * randomDouble + lowerBounds[it]
                }
                val dsElement = DatasetElement(
                    inputVector,
                    function(inputVector)
                )
                ds.dataPoints.add(dsElement)
            }
            ds.function = function
            return ds
        }
    }
}