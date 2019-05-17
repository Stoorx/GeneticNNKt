package model.genetic

import util.SingleRandom

class GeneticSolver {
    companion object {
        var mutationRate: Double = 0.5
    }

    val population: ArrayList<Model> = ArrayList()

    fun evolutionStep() {
        val newPopulation: ArrayList<Model> = ArrayList()
        newPopulation.addAll(population)

        population.forEach {
            newPopulation.add(
                it.crossover(population[SingleRandom.nextInt(population.size)])
            )
        }

        // TODO: sort and select
    }
}