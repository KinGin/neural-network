import main.datainput.UserDataImporter
import main.neuralnetwork.CustomMaxNormalizer
import main.neuralnetwork.NeuralNetwork
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.neuroph.util.data.norm.MaxNormalizer
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_UP
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

public class NeuralNetworkTest {

    private val userDataImporter = UserDataImporter()
    private val testUserDataPath = "/Users/kingin/code/neuro/src/test/resources/input/users.csv"
    private val dataRows = userDataImporter.getDataRowsAsDataSet(testUserDataPath)
    private val targetdDataRows = userDataImporter.getDataRowsAsDataSet(testUserDataPath)
    val neuralNetwork = NeuralNetwork(dataRows.inputSize, dataRows.outputSize)
    val normalizer = CustomMaxNormalizer()


    @Test
    public fun dataSetTest() {
        neuralNetwork.train(dataRows, 1000)

        normalizer.normalize(targetdDataRows)

        val targetRowOne = targetdDataRows.getRowAt(0)
        val targetRowTwo = targetdDataRows.getRowAt(1)

        val result = neuralNetwork.estimate(targetRowOne)
        val resultTwo = neuralNetwork.estimate(targetRowTwo)

        Assertions.assertEquals(targetRowOne.desiredOutput.map{ d: Double -> BigDecimal(d).setScale(2, ROUND_UP)}.toCollection(ArrayList<BigDecimal>()), resultTwo.map{ d: Double -> BigDecimal(d).setScale(2, ROUND_UP)}.toCollection(ArrayList<BigDecimal>()))
        Assertions.assertEquals(Arrays.toString(targetRowTwo.desiredOutput), Arrays.toString(resultTwo))

    }
}
