package main

import main.datainput.UserDataImporter
import main.neuralnetwork.NeuralNetwork
import java.util.*

fun main(args: Array<String>) {

    val userDataImporter = UserDataImporter()
    val scanner = Scanner(System.`in`)
    var neuralNetwork: NeuralNetwork

    println("Simple neural network started")
    print("> ")

    var input: String = ""

    while (input != "q"){
        input = scanner.nextLine()
        if(input.startsWith("r")){
            val params =  input.split(" ")
            if(params.size == 2){
                val dataRows =  userDataImporter.getDataRowsAsDataSet(params[1])
                neuralNetwork = NeuralNetwork(dataRows.inputSize, dataRows.outputSize)
                neuralNetwork.train(dataRows, 1000)
            }
        }
        println(input)
    }
}
