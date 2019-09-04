import main.datainput.UserDataImporter
import main.neuralnetwork.NeuralNetwork
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.neuroph.util.data.norm.MaxNormalizer
import java.util.*

public class NeuralNetworkTest {

    private val userDataImporter = UserDataImporter()
    private val testUserDataPath = "/Users/kingin/code/neuro/src/test/resources/input/users.csv"
    private val dataRows = userDataImporter.getDataRowsAsDataSet(testUserDataPath)
    private val targetDataRows = userDataImporter.getDataRowsAsDataSet(testUserDataPath)
    val neuralNetwork = NeuralNetwork(dataRows.inputSize, dataRows.outputSize)
    val normalizer = MaxNormalizer()


    @Test
    public fun dataSetTest() {
        neuralNetwork.train(dataRows, 1000)

        normalizer.normalize(targetDataRows)

        val targetRowOne = targetDataRows.getRowAt(0)
        val targetRowTwo = targetDataRows.getRowAt(1)

        val result = neuralNetwork.estimate(targetRowOne)
        val resultTwo = neuralNetwork.estimate(targetRowTwo)

        Assertions.assertEquals(Arrays.toString(targetRowOne.desiredOutput), Arrays.toString(result))
        Assertions.assertEquals(Arrays.toString(targetRowTwo.desiredOutput), Arrays.toString(resultTwo))

    }
}
