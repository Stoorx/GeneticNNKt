package model.neural

import model.fourier.FourierHypersurface
import model.fourier.FourierSeries
import java.lang.Exception

interface Neuron {
    fun calculate(): Double
    val inputsCount: Int
}

abstract class ChainableNeuron protected constructor() : Neuron {
    protected val previousNeurons: ArrayList<Neuron> = ArrayList()
    final override val inputsCount: Int
        get() = previousNeurons.size

    protected constructor(neurons: Collection<Neuron>) : this() {
        this.addPreviousNeurons(neurons)
    }

    protected open fun addPreviousNeuron(neuron: Neuron) {
        previousNeurons.add(neuron)
    }

    protected open fun addPreviousNeurons(neurons: Collection<Neuron>) {
        previousNeurons.addAll(neurons)
    }
}

class InputNeuron : Neuron {
    var value: Double = 0.0

    override fun calculate(): Double = value

    override val inputsCount: Int
        get() = 1
}

class InternalNeuron : ChainableNeuron() {
    protected val activationFunction: FourierHypersurface = FourierHypersurface()
    var t: Double = 0.0

    protected var inputCache: DoubleArray? = null
    protected var cachedResult: Double = 0.0

    override fun calculate(): Double {
        val inputValues = DoubleArray(inputsCount) {
            previousNeurons[it].calculate()
        }

        return if (inputCache?.contentEquals(inputValues) == true) {
            cachedResult
        } else {
            inputCache = inputValues
            activationFunction.calculate(inputValues.asList(), t).also { cachedResult = it }
        }
    }

    fun addPreviousNeuron(neuron: Neuron, consumer: () -> FourierSeries) {
        super.addPreviousNeuron(neuron)
        activationFunction.dimensions.add(consumer())
        inputCache = null
    }

    fun addPreviousNeurons(neurons: Collection<Neuron>, consumer: (Int) -> FourierSeries) {
        super.addPreviousNeurons(neurons)
        activationFunction.dimensions.addAll(Array(neurons.size, consumer))
        inputCache = null
    }
}


class OutputNeuron : ChainableNeuron() {
    protected val weights: MutableList<Double> = ArrayList()

    protected var inputCache: DoubleArray? = null
    protected var cachedResult: Double = 0.0

    override fun calculate(): Double {
        val inputValues = DoubleArray(inputsCount) {
            previousNeurons[it].calculate()
        }

        return if (inputCache?.contentEquals(inputValues) == true) {
            cachedResult
        } else {
            var result: Double = 0.0
            inputValues.forEachIndexed { index, value ->
                result += value * weights[index]
            }

            inputCache = inputValues
            cachedResult = result
            result
        }
    }
}