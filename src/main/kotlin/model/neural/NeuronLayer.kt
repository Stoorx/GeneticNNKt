package model.neural

import util.Visitable
import util.Visitor

class NeuronLayer : Visitable {
    val neurons: MutableList<Neuron> = ArrayList()

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}