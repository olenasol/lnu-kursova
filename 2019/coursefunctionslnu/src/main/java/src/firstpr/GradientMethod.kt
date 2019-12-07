import java.lang.Math.sqrt
import java.util.Arrays
import java.util.stream.IntStream


fun gradientMethod(matrix: Array<DoubleArray>, vector: DoubleArray, inx0: DoubleArray,eps: Double): DoubleArray {
    val n = vector.size
    val x1 = DoubleArray(n)
    var x0 = inx0
    val temp = multiplyMatrixByVector(matrix, x0)
    var r0 = DoubleArray(n, { i -> vector[i] - temp[i] })
    var p0 = r0
    val r1 = DoubleArray(n)
    val p1 = DoubleArray(n)
    var alphak: Double
    var betak: Double
    var k = 0
    var rsold =0.0
    for( i in 0 until r0.size){
        rsold+=r0[i]*r0[i]
    }
    for(i in 0 until vector.size) {
        val temp2 = multiplyMatrixByVector(matrix, p0)
        val temp3 = multiplyVectorByVector(temp2, p0)
        alphak = rsold / temp3
        for (j in 0..(n - 1)) {
            x1[j] = x0[j] + alphak * p0[j]
            r1[j] = r0[j] - alphak * temp2[j]
        }
        var rsnew =0.0
        for( j in 0 until r0.size){
            rsnew+=r1[j]*r1[j]
        }
        if (sqrt(rsnew) < eps)
            break
        for (j in 0..(n - 1))
            p1[j] = r1[j] + (rsnew / rsold) * p0[j]
        p0 = p1
        r0 = r1
        x0 = x1
        rsold = rsnew
        k++
    }
    return x1
}
fun gradientMethodMatrix(matrix: Supermatrix, vector: DoubleArray, inx0: DoubleArray,eps: Double): DoubleArray {
    val n = vector.size
    val x1 = DoubleArray(n)
    var x0 = inx0
    val temp = BlockClusterTree.MultHMatrixByVector(matrix, x0)
    var r0 = DoubleArray(n, { i -> vector[i] - temp[i] })
    var p0 = r0
    val r1 = DoubleArray(n)
    val p1 = DoubleArray(n)
    var alphak: Double
    var betak: Double
    var k = 0
    var rsold =0.0
    for( i in 0 until r0.size){
        rsold+=r0[i]*r0[i]
    }
    for(i in 0 until vector.size) {
        val temp2 = BlockClusterTree.MultHMatrixByVector(matrix, p0)
        val temp3 = multiplyVectorByVector(temp2, p0)
        alphak = rsold / temp3
        for (j in 0..(n - 1)) {
            x1[j] = x0[j] + alphak * p0[j]
            r1[j] = r0[j] - alphak * temp2[j]
        }
        var rsnew =0.0
        for( j in 0 until r0.size){
            rsnew+=r1[j]*r1[j]
        }
        if (sqrt(rsnew) < eps)
            break
        for (j in 0..(n - 1))
            p1[j] = r1[j] + (rsnew / rsold) * p0[j]
        p0 = p1
        r0 = r1
        x0 = x1
        rsold = rsnew
        k++
    }
    return x1
}

fun multiply(matrix: Array<DoubleArray>, vector: DoubleArray): DoubleArray {
    return Arrays.stream(matrix)
            .mapToDouble { row ->
                IntStream.range(0, row.size)
                        .mapToDouble({ col -> row[col] * vector[col] })
                        .sum()
            }.toArray()
}
fun gradientMethodMatrix(matrix: src.secondpr.Supermatrix, vector: DoubleArray, inx0: DoubleArray,eps: Double): DoubleArray {
    val n = vector.size
    val x1 = DoubleArray(n)
    var x0 = inx0
    val temptest =multiply(src.secondpr.BlockClusterTree.getNormalMatrix(matrix), x0)
    val temp =src.secondpr.BlockClusterTree.MultHMatrixByVector((matrix), x0, DoubleArray(x0.size))
    var r0 = DoubleArray(n, { i -> vector[i] - temp[i] })
    var p0 = r0
    val r1 = DoubleArray(n)
    val p1 = DoubleArray(n)
    var alphak: Double
    var betak: Double
    var k = 0
    var rsold =0.0
    for( i in 0 until r0.size){
        rsold+=r0[i]*r0[i]
    }
    for(i in 0 until vector.size) {

        val temp2test=multiply(src.secondpr.BlockClusterTree.getNormalMatrix(matrix), p0)
        val temp2 = src.secondpr.BlockClusterTree.MultHMatrixByVector((matrix), p0, DoubleArray(p0.size))
        val temp3 = multiplyVectorByVector(temp2, p0)
        alphak = rsold / temp3
        for (j in 0..(n - 1)) {
            x1[j] = x0[j] + alphak * p0[j]
            r1[j] = r0[j] - alphak * temp2[j]
        }
        var rsnew =0.0
        for( j in 0 until r0.size){
            rsnew+=r1[j]*r1[j]
        }
        if (sqrt(rsnew) < eps)
            break
        for (j in 0..(n - 1))
            p1[j] = r1[j] + (rsnew / rsold) * p0[j]
        p0 = p1
        r0 = r1
        x0 = x1
        rsold = rsnew
        k++
    }
    return x1
}
//TODO indeces are messed up here
fun multiplyMatrixByVector(matrix: Array<DoubleArray>, vector: DoubleArray): DoubleArray {
    val result = DoubleArray(matrix.size)
    for (i in 0..(matrix.size - 1)) {
        for (j in 0..(matrix[i].size - 1))
            result[i] += matrix[i][j] * vector[j]
    }
    return result
}

fun multiplyMatricesV3(firstMatrix: Array<DoubleArray>, secondMatrix: Array<DoubleArray>): Array<DoubleArray> {
    val r1 = firstMatrix.size
    val c1 = firstMatrix[0].size
    val c2 = secondMatrix[0].size
    val product = Array(r1) { DoubleArray(c2) }
    for (i in 0 until r1) {
        for (j in 0 until c2) {
            for (k in 0 until c1) {
                product[i][j] += firstMatrix[i][k] * secondMatrix[k][j]
            }
        }
    }

    return product
}

fun transposeMatrix(m: Array<DoubleArray>): Array<DoubleArray> {
    val temp = Array(m[0].size) { DoubleArray(m.size) }
    for (i in m.indices)
        for (j in 0 until m[0].size)
            temp[j][i] = m[i][j]
    return temp
}
fun multiplyVectorByVector(vector1: DoubleArray, vector2: DoubleArray): Double {
    var result = 0.0
    for (i in 0..(vector1.size - 1)) {
        result += vector1[i] * vector2[i]
    }
    return result
}