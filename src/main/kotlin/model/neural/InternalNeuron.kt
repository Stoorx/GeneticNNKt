package model.neural

import model.fourier.FourierHypersurface
import model.fourier.FourierSeries
import util.Visitor

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

    override fun accept(v: Visitor) {
        v.visit(this)
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