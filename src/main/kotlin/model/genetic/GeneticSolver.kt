package model.genetic

import model.Dataset
import util.SingleRandom
import java.lang.Math.pow

class GeneticSolver {
    companion object {
        var mutationRate: Double = 0.5
        var goodRatio: Double = 0.3
        var maxPopulation = 100
    }

    var dataset: Dataset? = null

    var population: ArrayList<Model> = ArrayList()

    fun calculateError(): Array<Pair<Model, Double>> {
        return Array<Pair<Model, Double>>(population.size) {
            val model = population[it]
            val dataPoint = (dataset ?: throw Exception("Dataset is null"))
                .dataPoints[SingleRandom.nextInt()]
            Pair(model, pow(model.calculate(dataPoint.inputs)[0] - dataPoint.output, 2.0))
        }
    }
    fun evolutionStep() {
        val newPopulation: ArrayList<Model> = ArrayList()
        newPopulation.addAll(population)

        population.forEach {
            newPopulation.add(
                it.crossover(population[SingleRandom.nextInt(population.size)])
            )
        }

        if (newPopulation.size > maxPopulation) {
            val pairs = Array<Pair<Model, Double>>(newPopulation.size) {
                val model = newPopulation[it]
                val dataPoint = (dataset ?: throw Exception("Dataset is null"))
                    .dataPoints[SingleRandom.nextInt()]
                Pair(model, pow(model.calculate(dataPoint.inputs)[0] - dataPoint.output, 2.0))
            }

            pairs.sortBy {
                it.second
            }
            val goodModels = pairs.take((newPopulation.size * goodRatio).toInt()).map {
                it.first
            }

            population = ArrayList<Model>(goodModels)

        } else {
            population = newPopulation
        }

    }
}