package main.neuralnetwork

import org.neuroph.core.Layer
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.Neuron
import org.neuroph.core.learning.LearningRule
import org.neuroph.util.ConnectionFactory


public class NeuralNetwork(inputSize: Number, outputSize: Number) {

    val neuralNetwork: NeuralNetwork<LearningRule> = NeuralNetwork<LearningRule>() // = Perceptron(inputSize.toInt(), outputSize.toInt(), TransferFunctionType.SIGMOID)

    init {

        neuralNetwork.addLayer(0, inputLayer(inputSize))
        for (item in hiddenLayers(listOf(Pair(1, inputSize))))
        {
            neuralNetwork.addLayer(item.key, item.value)
        }

        neuralNetwork.addLayer(neuralNetwork.layersCount + 1, outputLayer(outputSize))
        fullConnectAllLayers(neuralNetwork)
    }

    private fun inputLayer(inputSize: Number): Layer = layerCreation(inputSize)
    private fun outputLayer(inputSize: Number): Layer = layerCreation(inputSize)

    private fun layerCreation(neuronAmount: Number): Layer {
        val layer = Layer()
        for (i in 0..neuronAmount.toInt())
        {
            layer.addNeuron(Neuron())
        }
        return layer
    }

    /**
     * hiddenlayer index + amount of neurons list
     * index should be distinct
     */
    private fun hiddenLayers(indexAmountList: List<Pair<Number, Number>>) : HashMap<Int, Layer> {
        val hiddenLayers = HashMap<Int, Layer>()
        for(initializationParams in indexAmountList) {
            hiddenLayers[initializationParams.first.toInt()] = layerCreation(initializationParams.second)
        }
        return hiddenLayers
    }

    private fun fullConnectAllLayers(neuralNetwork: NeuralNetwork<LearningRule>) {
        if (neuralNetwork.layersCount < 2) return

        var from = 0
        var to = 1

        while (to < neuralNetwork.layersCount) {
            ConnectionFactory.fullConnect(neuralNetwork.getLayerAt(from), neuralNetwork.getLayerAt(to))
            ++from
            ++to
        }
    }   
}
