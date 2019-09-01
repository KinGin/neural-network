package main.neuralnetwork

import org.neuroph.core.Layer
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.Neuron
import org.neuroph.core.data.DataSet
import org.neuroph.core.data.DataSetRow
import org.neuroph.core.learning.LearningRule
import org.neuroph.nnet.learning.BackPropagation
import org.neuroph.util.ConnectionFactory
import org.neuroph.util.TransferFunctionType
import java.util.*


public class NeuralNetwork(inputSize: Number, outputSize: Number) {

    val outputPath = "src/main/resources/output/weight.csv"

    val neuralNetwork: NeuralNetwork<LearningRule> =
        NeuralNetwork<LearningRule>() // = Perceptron(inputSize.toInt(), outputSize.toInt(), TransferFunctionType.SIGMOID)

    init {
        neuralNetwork.addLayer(0, inputLayer(inputSize))
        for (item in hiddenLayers(listOf(Pair(1, inputSize)))) {
            neuralNetwork.addLayer(item.key, item.value)
        }

        neuralNetwork.addLayer(neuralNetwork.layersCount, outputLayer(outputSize))
        fullConnectAllLayers(neuralNetwork)
        neuralNetwork.inputNeurons = neuralNetwork.getLayerAt(0).neurons
        neuralNetwork.outputNeurons = neuralNetwork.getLayerAt(neuralNetwork.layersCount - 1).neurons
    }

    private fun inputLayer(inputSize: Number): Layer = layerCreation(inputSize)
    private fun outputLayer(inputSize: Number): Layer = layerCreation(inputSize)

    private fun layerCreation(neuronAmount: Number): Layer {
        val layer = Layer()
        for (i in 0 until neuronAmount.toInt()) {
            layer.addNeuron(Neuron())
        }
        return layer
    }

    /**
     * hiddenlayer index + amount of neurons list
     * index should be distinct
     */
    private fun hiddenLayers(indexAmountList: List<Pair<Number, Number>>): HashMap<Int, Layer> {
        val hiddenLayers = HashMap<Int, Layer>()
        for (initializationParams in indexAmountList) {
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

    public fun train(dataSet: DataSet, iterations: Number) {
        val backPropagation = BackPropagation()
        backPropagation.maxIterations = iterations.toInt()
        neuralNetwork.learn(dataSet, backPropagation)
        neuralNetwork.save(outputPath)
    }

    public fun estimate(inputSet: DataSetRow): DoubleArray {
        println("input " + inputSet.input)
        neuralNetwork.setInput(*inputSet.input)
        neuralNetwork.calculate()
        println("outPut " + Arrays.toString(neuralNetwork.output))
        return neuralNetwork.output
    }

    public fun estimate(vararg inputVector: Double) {
        println("input $inputVector")
        neuralNetwork.setInput(*inputVector)
        neuralNetwork.calculate()
        println("outPut " + Arrays.toString(neuralNetwork.output))
    }
}
