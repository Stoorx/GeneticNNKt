package model.neural

abstract class ChainableNeuron protected constructor() : Neuron {
    val previousNeurons: ArrayList<Neuron> = ArrayList()
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