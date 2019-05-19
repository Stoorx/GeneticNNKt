package model.genetic

import model.fourier.FourierElement
import model.fourier.FourierHypersurface
import model.fourier.FourierSeries
import model.fourier.PitchedFourierSeries
import model.neural.*
import util.SingleRandom

fun Model.crossover(other: Model): Model {
    val model = Model()
    for (i in 0 until layers.size) {
        model.layers.add(
            layers[i].crossover(other.layers[i])
        )
    }

    for (i in 1 until model.layers.size) {
        model.layers[i].neurons.forEach {
            if (it is ChainableNeuron) {
                it.previousNeurons.addAll(layers[i - 1].neurons)
            }
        }
    }
    return model
}

private fun NeuronLayer.crossover(other: NeuronLayer): NeuronLayer {
    val layer = NeuronLayer()
    for (i in 0 until neurons.size) {
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

                neuron.t =
                    if (SingleRandom.nextDouble() > GeneticSolver.mutationRate)
                        (this.t + other.t) / 2
                    else
                        SingleRandom.nextDouble()

                neuron.activationFunction =
                    FourierHypersurface(activationFunction.arity) {
                        activationFunction.dimensions[it].crossover(other.activationFunction.dimensions[it])
                    }

                return neuron
            }
            is OutputNeuron -> {
                other as OutputNeuron

                val neuron = OutputNeuron()

                for (i in 0 until this.weights.size) {
                    neuron.weights.add(
                        if (SingleRandom.nextDouble() > GeneticSolver.mutationRate)
                            (this.weights[i] + other.weights[i]) / 2
                        else
                            SingleRandom.nextDouble()
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

            pfs.pitch =
                if (SingleRandom.nextDouble() > GeneticSolver.mutationRate)
                    (this.pitch + other.pitch) / 2
                else
                    SingleRandom.nextDouble()

            fs = pfs
        }


        for (i in 0 until this.elements.size) {
            fs.elements.add(
                FourierElement(
                    if (SingleRandom.nextDouble() > GeneticSolver.mutationRate)
                        (this.elements[i].amplitude + other.elements[i].amplitude) / 2
                    else
                        SingleRandom.nextDouble(),
                    if (SingleRandom.nextDouble() > GeneticSolver.mutationRate)
                        (this.elements[i].phase + other.elements[i].phase) / 2
                    else
                        SingleRandom.nextDouble()
                )
            )
        }

        return fs
    } else {
        throw FourierSeriesCrossoverException("Incompatible types of fourier series")
    }
}
