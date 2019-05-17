package model.genetic

import model.fourier.FourierElement
import model.fourier.FourierHypersurface
import model.fourier.FourierSeries
import model.fourier.PitchedFourierSeries
import model.neural.*

fun Model.crossover(other: Model): Model {
    val model = Model()
    for (i in 0..layers.size) {
        model.layers.add(
            layers[i].crossover(other.layers[i])
        )
    }

    for (i in 1..layers.size) {
        layers[i].neurons.forEach {
            if (it is ChainableNeuron) {
                it.previousNeurons.addAll(layers[i - 1].neurons)
            }
        }
    }
    return model
}

private fun NeuronLayer.crossover(other: NeuronLayer): NeuronLayer {
    val layer = NeuronLayer()
    for (i in 0..neurons.size) {
        layer.neurons.add(
            neurons[i].crossover(other.neurons[i])
        )
    }

    return layer
}

private fun Neuron.crossover(other: Neuron): Neuron {
    if (this.javaClass == other.javaClass) {
        when (this) {
            is InternalNeuron -> {
                other as InternalNeuron

                val neuron = InternalNeuron()

                neuron.t = (this.t + other.t) / 2

                neuron.activationFunction =
                    FourierHypersurface(activationFunction.arity) {
                        activationFunction.dimensions[it].crossover(other.activationFunction.dimensions[it])
                    }

                return neuron
            }
            is OutputNeuron -> {
                other as OutputNeuron

                val neuron = OutputNeuron()

                for (i in 0..this.weights.size) {
                    neuron.weights.add(
                        (this.weights[i] + other.weights[i]) / 2
                    )
                }

                return neuron
            }
            is InputNeuron -> {
                other as InputNeuron

                return InputNeuron()
            }
            else -> {
                throw NeuronsCrossoverException("Unknown types of neurons")
            }
        }
    } else {
        throw NeuronsCrossoverException("Incompatible types of neurons")
    }
}

private fun FourierSeries.crossover(other: FourierSeries): FourierSeries {
    if (this.javaClass == other.javaClass) {
        lateinit var fs: FourierSeries

        if (this is PitchedFourierSeries) {
            other as PitchedFourierSeries

            val pfs = PitchedFourierSeries()
            pfs.pitch = (this.pitch + other.pitch) / 2
            fs = pfs
        }


        for (i in 0..this.elements.size) {
            fs.elements.add(
                FourierElement(
                    (this.elements[i].amplitude + other.elements[i].amplitude) / 2,
                    (this.elements[i].phase + other.elements[i].phase) / 2
                )
            )
        }

        return fs
    } else {
        throw FourierSeriesCrossoverException("Incompatible types of fourier series")
    }
}
