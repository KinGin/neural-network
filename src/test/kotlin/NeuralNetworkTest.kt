import main.datainput.UserDataImporter
import main.neuralnetwork.NeuralNetwork
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

public class NeuralNetworkTest {

    private val userDataImporter = UserDataImporter()
    private val testUserDataPath = "/Users/kingin/code/neuro/src/test/resources/input/users.csv"
    private val dataRows =  userDataImporter.getDataRowsAsDataSet(testUserDataPath)
    val neuralNetwork = NeuralNetwork(dataRows.inputSize, dataRows.outputSize)


    @Test
    public fun dataSetTest() {
        neuralNetwork.train(dataRows, 1000)

        val targetRowOne = dataRows.getRowAt(0)
        val targetRowTwo = dataRows.getRowAt(1)

        val result = neuralNetwork.estimate(targetRowOne)
        val resultTwo = neuralNetwork.estimate(targetRowTwo)

        Assertions.assertEquals(targetRowOne.desiredOutput, result)
        Assertions.assertEquals(targetRowTwo.desiredOutput, resultTwo)

    }
}
