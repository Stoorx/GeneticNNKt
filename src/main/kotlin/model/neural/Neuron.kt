package model.neural

import util.Visitable

interface Neuron : Visitable {
    fun calculate(): Double
    val inputsCount: Int
}


