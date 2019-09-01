package main.datainput

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import org.neuroph.core.data.DataSet
import org.neuroph.core.data.DataSetRow
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class UserDataImporter {

    public fun getDataRows(absoluteFilePath: String): Pair<List<String>, ArrayList<Pair<ArrayList<Double>, ArrayList<Double>>>>{
        val result: ArrayList<Pair<ArrayList<Double>, ArrayList<Double>>> = ArrayList()
        var title: ArrayList<String>
        try {
            val csvReader = CSVReaderBuilder(BufferedReader(FileReader(absoluteFilePath)))
                .withCSVParser(CSVParserBuilder().withSeparator(';').build())
                .build()

            title = ArrayList(parseInputParams(ArrayList(csvReader.readNext().toList())))

            var line: Array<String>? = csvReader.readNext()

            while (line != null) {
                result.add( Pair( stringListToDoubleArray(parseInputParams(line.take(13))), stringListToDoubleArray(line.takeLast(42))))
                line = csvReader.readNext()
            }
            csvReader.close()
        } catch (e: IOException) {
            throw IOException(e)
        }
        return Pair(title, result)
    }

    public fun getDataRowsAsDataSet(absoluteFilePath: String): DataSet {
        val dataRows = getDataRows(absoluteFilePath)
        return convertToDataSet(dataRows.second, dataRows.first)
    }

    private fun convertToDataSet(dataSet: ArrayList<Pair<ArrayList<Double>, ArrayList<Double>>>, header: List<String>): DataSet {
        val result: DataSet = DataSet(dataSet[0].first.size, dataSet[0].second.size)
        result.columnNames = header.toTypedArray()
        for (dataRow in dataSet){
            result.addRow(DataSetRow(dataRow.first, dataRow.second))
        }
        return result
    }

    private fun stringListToDoubleArray(list: List<String>) : ArrayList<Double> {
        return ArrayList(list.map { item -> item.replace(',','.').toDouble() })
    }

    public fun parseInputParams(list: List<String>, skipColumnsWithIndex: List<Number> = listOf(3)): List<String>{

        val result = ArrayList<String>()
        list.forEachIndexed{index, value ->
            if(!skipColumnsWithIndex.contains(index)){
                result.add(value)
            }
        }
        return result
    }
}
