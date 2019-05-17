package util

import model.fourier.FourierElement
import model.fourier.FourierHypersurface
import model.fourier.FourierSeries
import model.fourier.PitchedFourierSeries
import model.neural.InputNeuron
import model.neural.InternalNeuron
import model.neural.NeuronLayer
import model.neural.OutputNeuron

interface Visitor {
    fun visit(e: FourierElement)
    fun visit(e: FourierHypersurface)
    fun visit(e: FourierSeries)
    fun visit(e: PitchedFourierSeries)
    fun visit(e: InternalNeuron)
    fun visit(e: InputNeuron)
    fun visit(e: OutputNeuron)
    fun visit(e: NeuronLayer)
}

