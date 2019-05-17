package model.neural

import util.Visitor

class InputNeuron : Neuron {
    var value: Double = 0.0

    override fun calculate(): Double = value

    override fun accept(v: Visitor) {
        v.visit(this)
    }

    override val inputsCount: Int
        get() = 1
}