package main.datainput

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class UserDataImporter {

    public fun getDataRows(absoluteFilePath: String): Unit {
        try {
            val csvReader = CSVReaderBuilder(BufferedReader(FileReader(absoluteFilePath)))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .build()

            var line: Array<String>? = csvReader.readNext()

            while (line != null) {
                println(line)
                line = csvReader.readNext()
            }
            csvReader.close()
        } catch (e: IOException) {
            throw IOException(e)
        }
    }
}
