package main.neuralnetwork

import org.neuroph.core.data.DataSet
import org.neuroph.core.data.DataSetRow
import org.neuroph.util.data.norm.Normalizer

/**
 * Max normalization method, which normalize data in regard to max element in training set (by columns)
 * Normalization is done according to formula:
 * normalizedVector[i] = vector[i] / abs(max[i])
 * @author Zoran Sevarac <sevarac></sevarac>@gmail.com>
 */
class CustomMaxNormalizer : Normalizer {
     var maxIn: DoubleArray = DoubleArray(0)
     var maxOut: DoubleArray = DoubleArray(0)


    override fun normalize(dataSet: DataSet) {

        findMaxVectors(dataSet)

        for (row in dataSet.rows) {
            val normalizedInput = normalizeMax(row.input, maxIn)
            row.input = normalizedInput
        }
    }


    /**
     * Finds max values for columns in input and output vector for given data set
     * @param dataSet
     */
    private fun findMaxVectors(dataSet: DataSet) {
        val inputSize = dataSet.inputSize
        val outputSize = dataSet.outputSize

        maxIn = DoubleArray(inputSize)
        for (i in 0 until inputSize) {
            maxIn[i] = java.lang.Double.MIN_VALUE
        }

        maxOut = DoubleArray(outputSize)
        for (i in 0 until outputSize)
            maxOut[i] = java.lang.Double.MIN_VALUE

        for (dataSetRow in dataSet.rows) {
            val input = dataSetRow.input
            for (i in 0 until inputSize) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i]
                }
            }

            val output = dataSetRow.desiredOutput
            for (i in 0 until outputSize) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i]
                }
            }

        }
    }


    fun normalizeMax(vector: DoubleArray, max: DoubleArray): DoubleArray {
        val normalizedVector = DoubleArray(vector.size)

        for (i in vector.indices) {
            normalizedVector[i] = vector[i] / max[i]
        }

        return normalizedVector
    }

}
