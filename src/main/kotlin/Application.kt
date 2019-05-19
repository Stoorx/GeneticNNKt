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

fun main() {
    try {
        val ds = Dataset.generateFromFunction(DoubleArray(1) { -10.0 }, DoubleArray(1) { 10.0 }, 150) {
            val x = it[0]
            log(1 + pow(E, x), E) //1
            // pow(E, x) / cos(x)
            //sqrt(1 + 4 * x + 12 * x * x)
        }
        val out = FileOutputStream("dataset.csv")
        out.write(ds.toString().toByteArray())

        val gs = GeneticSolver()
        gs.dataset = ds
        GeneticSolver.maxPopulation = 100
        GeneticSolver.mutationRate = 0.15
        GeneticSolver.goodRatio = 0.40

        val startPopulationCount = 20
        val layersCount = 4
        val internalNeuronsInLayer = 3
        val activationLength = 3
        val inputNeuronsCount = 1
        val outputNeuronsCount = 1

        for (i in 0 until startPopulationCount) {
            println("Initializing model #$i")
            val model = Model()
            for (l in 0 until layersCount) {
                val nl = NeuronLayer()
                when (l) {
                    0 -> {
                        for (n in 0 until inputNeuronsCount) {
                            nl.neurons.add(InputNeuron())
                        }
                    }
                    layersCount - 1 -> {
                        for (n in 0 until outputNeuronsCount) {
                            val on = OutputNeuron()
                            on.addPreviousNeurons(model.layers[l - 1].neurons) {
                                SingleRandom.nextDouble()
                            }
                            nl.neurons.add(on)
                        }
                    }
                    else -> {
                        for (n in 0 until internalNeuronsInLayer) {
                            val intN = InternalNeuron()
                            intN.t = 0.10 // default 1.0
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

        val eSteps: Int = 1000
        var stepCounter = 0
        for (s in 0 until eSteps) {
            val errors = gs.calculateError().sortedBy { it.second }
            errors.forEachIndexed { index, pair ->
                print("${pair.second}, ")
            }
            println()
            //println("$stepCounter, ${errors[0].second}")
            if (stepCounter % 500 == 0 || eSteps - 1 == stepCounter) {
                val outSb = StringBuilder()
                val start = -10.0
                val stop = 10.0
                val step = 0.1

                var cur = start
                while (cur <= stop) {
                    val input = DoubleArray(1) { cur }
                    val real = ds.function?.invoke(input)!!
                    val predict = errors.first().first.calculate(input)

                    outSb.appendln(
                        "$cur, ${real}, ${predict[0]}, ${pow(real - predict[0], 2.0)}"
                    )
                    cur += step
                }

                val outResults = FileOutputStream("out$stepCounter.csv")
                outResults.write(outSb.toString().toByteArray())
            }
            gs.evolutionStep()
            stepCounter++
        }


    } catch (e: Throwable) {
        println(e.message)
    }
}