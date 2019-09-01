import main.datainput.UserDataImporter
import org.junit.jupiter.api.Test

public class fileReaderTest {

    @Test
    public fun testFileReader(){
        val userdataImporter = UserDataImporter()
        userdataImporter.getDataRows("src/test/resources/input/users.csv")
    }
}
