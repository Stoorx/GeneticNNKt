package model.genetic

import model.neural.InputNeuron
import model.neural.NeuronLayer


class Model {
    val layers: ArrayList<NeuronLayer> = ArrayList()
    fun setInput(values: DoubleArray) {
        layers[0].neurons.forEachIndexed { index, neuron ->
            if (neuron is InputNeuron)
                neuron.value = values[index]
        }
    }

    fun calculate(values: DoubleArray): DoubleArray {
        setInput(values)
        val lastLayer: NeuronLayer = layers.last()
        val outputs = ArrayList<Double>()

        lastLayer.neurons.forEach {
            outputs.add(it.calculate())
        }

        return outputs.toDoubleArray()
    }
}

