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
                it.previousNeurons.addAll(model.layers[i - 1].neurons)
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

//                neuron.t =
//                    if (SingleRandom.doubleZeroToOne() > GeneticSolver.mutationRate)
//                        (this.t + other.t) / 2
//                    else
//                        neuron.t * SingleRandom.nextDouble()


                val fhs = FourierHypersurface(activationFunction.arity) {
                    activationFunction
                        .dimensions[it]
                        .crossover(
                            other
                                .activationFunction
                                .dimensions[it]
                        )
                }
                neuron.activationFunction = fhs
                return neuron
            }
            is OutputNeuron -> {
                other as OutputNeuron

                val neuron = OutputNeuron()

                for (i in 0 until this.weights.size) {
                    neuron.weights.add(
                        if (SingleRandom.doubleZeroToOne() < GeneticSolver.mutationRate)
                            (this.weights[i] + other.weights[i]) / 2
                        else
                            (this.weights[i] + other.weights[i]) / (2 * SingleRandom.nextDouble())
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
                if (SingleRandom.doubleZeroToOne() < GeneticSolver.mutationRate)
                    (this.pitch + other.pitch) / 2
                else
                    (this.pitch + other.pitch) / (SingleRandom.nextDouble())

            pfs.b = if (SingleRandom.doubleZeroToOne() < GeneticSolver.mutationRate)
                (this.b + other.b) / 2
            else
                (this.b + other.b) / (SingleRandom.nextDouble())

            fs = pfs
        }


        for (i in 0 until this.elements.size) {
            fs.elements.add(
                FourierElement(
                    if (SingleRandom.doubleZeroToOne() < GeneticSolver.mutationRate)
                        (this.elements[i].amplitude + other.elements[i].amplitude) / 2
                    else
                        (this.elements[i].amplitude + other.elements[i].amplitude) / (2 * SingleRandom.nextDouble()),

                    if (SingleRandom.doubleZeroToOne() < GeneticSolver.mutationRate)
                        (this.elements[i].phase + other.elements[i].phase) / 2
                    else
                        (this.elements[i].phase + other.elements[i].phase) / (2 * SingleRandom.nextDouble())
                )
            )
        }

        return fs
    } else {
        throw FourierSeriesCrossoverException("Incompatible types of fourier series")
    }
}
