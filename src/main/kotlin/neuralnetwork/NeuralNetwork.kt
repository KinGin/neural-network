package main.neuralnetwork

import org.neuroph.core.Layer
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.Neuron
import org.neuroph.core.data.DataSet
import org.neuroph.core.data.DataSetRow
import org.neuroph.core.events.LearningEventListener
import org.neuroph.core.input.WeightedSum
import org.neuroph.core.learning.LearningRule
import org.neuroph.core.transfer.Tanh
import org.neuroph.nnet.learning.BackPropagation
import org.neuroph.util.ConnectionFactory
import org.neuroph.util.data.norm.MaxNormalizer
import java.util.*
import org.neuroph.nnet.learning.MomentumBackpropagation
import org.neuroph.core.events.LearningEvent
import org.neuroph.core.transfer.Linear
import org.neuroph.core.transfer.TransferFunction
import java.math.RoundingMode
import java.math.BigDecimal


public class NeuralNetwork(inputSize: Number, outputSize: Number) {

    val outputPath = "src/main/resources/output/weight.csv"
    val normalizer = MaxNormalizer()


    val neuralNetwork: NeuralNetwork<LearningRule> =
        NeuralNetwork<LearningRule>() // = Perceptron(inputSize.toInt(), outputSize.toInt(), TransferFunctionType.SIGMOID)

    init {
        neuralNetwork.addLayer(0, inputLayer(inputSize))
        //for (item in hiddenLayers(listOf(Pair(1, inputSize.toInt() * 10),  Pair(2, inputSize.toInt()*3), Pair(3, inputSize.toInt()/2)))) {
        //0.0902214428242789 for (item in hiddenLayers(listOf(Pair(1, inputSize.toInt() * 5), Pair(2, inputSize.toInt()*4), Pair(3, inputSize.toInt()*3)))) {
        for (item in hiddenLayers(listOf(Pair(1, inputSize.toInt() * 5), Pair(2, inputSize.toInt()*4), Pair(3, inputSize.toInt()*3),Pair(4, inputSize.toInt()*2)))) {
            neuralNetwork.addLayer(item.key, item.value)
        }
        neuralNetwork.learningRule = MomentumBackpropagation()

        neuralNetwork.addLayer(neuralNetwork.layersCount, outputLayer(outputSize))
        fullConnectAllLayers(neuralNetwork)
        neuralNetwork.inputNeurons = neuralNetwork.getLayerAt(0).neurons
        neuralNetwork.outputNeurons = neuralNetwork.getLayerAt(neuralNetwork.layersCount - 1).neurons
        //neuralNetwork.randomizeWeights(1.0, 999999.0)
    }

    private fun inputLayer(inputSize: Number): Layer = layerCreation(inputSize)
    private fun outputLayer(inputSize: Number): Layer = layerCreation(inputSize, Linear())

    private fun layerCreation(neuronAmount: Number, transferFunction: TransferFunction = Tanh()): Layer {
        val layer = Layer()
        for (i in 0 until neuronAmount.toInt()) {
            layer.addNeuron(Neuron(WeightedSum(), transferFunction))
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
        normalizer.normalize(dataSet)

        val learningRule = neuralNetwork.learningRule as MomentumBackpropagation
        learningRule.addListener { handleLearningEvent(it) }
        learningRule.learningRate = 0.01
        learningRule.maxError = 0.01
        learningRule.maxIterations = iterations.toInt()


        //val backPropagation = BackPropagation()
        //backPropagation.maxIterations = iterations.toInt()
        neuralNetwork.learn(dataSet)
        neuralNetwork.save(outputPath)
    }

    public fun estimate(inputSet: DataSetRow): DoubleArray {
        println("input " + Arrays.toString(inputSet.input))
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


    fun handleLearningEvent(event: LearningEvent) {
        val bp = event.source as BackPropagation
        if (event.eventType == LearningEvent.Type.LEARNING_STOPPED) {
            val error = bp.totalNetworkError
            println("Training completed in " + bp.currentIteration + " iterations, ")
            System.out.println("With total error: " + formatDecimalNumber(error))
        } else {
            println("Iteration: " + bp.currentIteration + " | Network error: " + bp.totalNetworkError)
        }
    }

    fun formatDecimalNumber(number: Double): String {
        return BigDecimal(number).setScale(4, RoundingMode.HALF_UP).toString()
    }

}
