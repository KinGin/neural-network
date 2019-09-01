import main.datainput.UserDataImporter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

public class DataParsingTest {

    val userdataImporter = UserDataImporter()

    @Test
    public fun testFileReader() {
        val parseTestData = userdataImporter.getDataRows("src/test/resources/input/users.csv")
        Assertions.assertEquals(508, parseTestData.second.size)
    }

    @Test
    public fun columnFilterTest() {
        val targetList = listOf("time", "date", "value_x", "value_y")
        val sourceList = listOf("time", "date", "rownum", "value_x", "value_y")

        Assertions.assertEquals(targetList, userdataImporter.parseInputParams(sourceList, listOf(2)))
    }

    @Test
    public fun columnFilterMultipleTest() {
        val targetList = listOf("date", "value_x")
        val sourceList = listOf("time", "date", "rownum", "value_x", "value_y")

        Assertions.assertEquals(targetList, userdataImporter.parseInputParams(sourceList, listOf(0, 2, 4)))
    }
}
