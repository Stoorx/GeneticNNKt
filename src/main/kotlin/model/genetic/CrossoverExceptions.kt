package model.genetic

open class CrossoverException(message: String) : Exception(message)

class NeuronsCrossoverException(message: String) : CrossoverException(message)
class FourierSeriesCrossoverException(message: String) : CrossoverException(message)
