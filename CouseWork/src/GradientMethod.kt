
fun gradientMethod(matrix: Array<DoubleArray>, vector: DoubleArray, inx0: DoubleArray): DoubleArray {
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
    while (k != n) {
        val temp1 = multiplyVectorByVector(r0, r0)
        val temp2 = multiplyMatrixByVector(matrix, p0)
        val temp3 = multiplyVectorByVector(temp2, p0)
        alphak = temp1 / temp3
        for (i in 0..(n - 1)) {
            x1[i] = x0[i] + alphak * p0[i]
            r1[i] = r0[i] - alphak * temp2[i]
        }
        betak = multiplyVectorByVector(r1, r1) / multiplyVectorByVector(r0, r0)
        for (i in 0..(n - 1))
            p1[i] = r1[i] + betak * p0[i]
        p0 = p1
        r0 = r1
        x0 = x1
        k++
    }
    return x1
}

fun gradientMethodMatrix(matrix: Supermatrix, vector: DoubleArray, inx0: DoubleArray): DoubleArray {
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
    while (k != n) {
        val temp1 = multiplyVectorByVector(r0, r0)
        val temp2 = BlockClusterTree.MultHMatrixByVector(matrix, p0)
        val temp3 = multiplyVectorByVector(temp2, p0)
        alphak = temp1 / temp3
        for (i in 0..(n - 1)) {
            x1[i] = x0[i] + alphak * p0[i]
            r1[i] = r0[i] - alphak * temp2[i]
        }
        betak = multiplyVectorByVector(r1, r1) / multiplyVectorByVector(r0, r0)
        for (i in 0..(n - 1))
            p1[i] = r1[i] + betak * p0[i]
        p0 = p1
        r0 = r1
        x0 = x1
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