package model.neural

import util.Visitor

class OutputNeuron : ChainableNeuron() {
    val weights: MutableList<Double> = ArrayList()

    protected var inputCache: DoubleArray? = null
    protected var cachedResult: Double = 0.0

    override fun accept(v: Visitor) {
        v.visit(this)
    }

    fun addPreviousNeuron(neuron: Neuron, weight: Double) {
        super.addPreviousNeuron(neuron)
        weights.add(weight)
        inputCache = null
    }

    fun addPreviousNeurons(neurons: Collection<Neuron>, consumer: (Int) -> Double) {
        super.addPreviousNeurons(neurons)
        weights.addAll(Array(neurons.size, consumer))
        inputCache = null
    }

    override fun calculate(): Double {
        val inputValues = DoubleArray(inputsCount) {
            previousNeurons[it].calculate()
        }

        return if (inputCache?.contentEquals(inputValues) == true) {
            cachedResult
        } else {
            var result = 0.0
            inputValues.forEachIndexed { index, value ->
                result += value * weights[index]
            }

            inputCache = inputValues
            cachedResult = result
            result
        }
    }
}