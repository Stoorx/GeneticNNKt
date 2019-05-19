import model.Dataset
import model.fourier.PitchedFourierSeries
import model.genetic.GeneticSolver
import model.genetic.Model
import model.neural.InputNeuron
import model.neural.InternalNeuron
import model.neural.NeuronLayer
import model.neural.OutputNeuron
import util.SingleRandom
import java.io.FileOutputStream
import java.lang.Math.pow
import kotlin.math.E
import kotlin.math.log

fun main(args: Array<String>) {
    val ds = Dataset.generateFromFunction(DoubleArray(1) { -10.0 }, DoubleArray(1) { 10.0 }, 100) {
        val x = it[0]
        log(1 + pow(E, x), E) //1
        // pow(E, x) / cos(x)
        //sqrt(1 + 4 * x + 12 * x * x)
    }
    val out = FileOutputStream("dataset.csv")
    out.write(ds.toString().toByteArray())

    val gs = GeneticSolver()
    gs.dataset = ds

    val startPopulationCount = 10
    val layersCount = 5
    val internalNeuronsInLayer = 10
    val activationLength = 5
    val inputNeuronsCount = 1
    val outputNeuronsCount = 1

    for (i in 0..startPopulationCount) {
        val model = Model()
        for (l in 0..layersCount) {
            val nl = NeuronLayer()
            when (l) {
                0 -> {
                    for (n in 0..inputNeuronsCount) {
                        nl.neurons.add(InputNeuron())
                    }
                }
                layersCount -> {
                    for (n in 0..outputNeuronsCount) {
                        val on = OutputNeuron()
                        on.addPreviousNeurons(model.layers[l - 1].neurons) {
                            SingleRandom.nextDouble()
                        }
                        nl.neurons.add(on)
                    }
                }
                else -> {
                    for (n in 0..internalNeuronsInLayer) {
                        val intN = InternalNeuron()
                        //intN.t = SingleRandom.nextDouble() // default 1.0
                        intN.addPreviousNeurons(model.layers[l - 1].neurons) {
                            PitchedFourierSeries.createFromRandom(activationLength)
                        }
                        nl.neurons.add(intN)
                    }
                }
            }
            model.layers.add(nl)
        }
        gs.population.add(model)
    }

    println(gs)

}