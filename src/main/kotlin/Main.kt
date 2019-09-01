package main

import main.datainput.UserDataImporter
import java.util.*

fun main(args: Array<String>) {

    val userDataImporter = UserDataImporter()
    val scanner = Scanner(System.`in`)
    println("Simple neural network started")
    print("> ")

    var input: String = ""

    while (input != "q"){
        print("> ")
        input = scanner.nextLine()
        if(input.startsWith("r")){
            val params =  input.split(" ")
            if(params.size == 2){
                userDataImporter.getDataRows(params[1])
            }
        }
        println(input)
    }
}
