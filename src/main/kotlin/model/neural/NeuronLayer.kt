package model.neural

import util.Visitable
import util.Visitor

class NeuronLayer : Visitable {
    val neurons: MutableList<Neuron> = ArrayList()

    fun addPreviousLayer() {

    }
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}