
fun un(x: Double, u: DoubleArray, n: Int): Double {
    var s = 0.0
    for (i in 0 until n) {

        s +=  phi(i, n, x) * u[i]
    }
    return s
}

fun yTochne(x: Double): Double {
    return 1.0
}

fun main() {

    val n = 32
    val k= 2
    var s = Supermatrix()
    val c = ClusterTree.buildClusterTree(n,k)
    try {
        s = BlockClusterTree.buildBlockClusterTree(c, c, s, n,k)
    } catch (e: Exception) {
        println("error")
    }
    val testMatrix =BlockClusterTree.getNormalMatrix(s)
    for (i in 0 until testMatrix.size){
        for (j in 0 until testMatrix[i].size){
            print(testMatrix[i][j].toString()+" ")

        }
        println()
    }
    val f = DoubleArray(n)
    val x0 = DoubleArray(n){0.0}
    println("-------------------")
    println("f")
    for (i in 0 until n) {
        f[i] = funderint(i.toDouble() / n.toDouble(),(i+1.0) / n.toDouble())
        println(f[i])
    }
    println("----------------")
    println("u_approx")
    val u = gradientMethodMatrix((s),f, DoubleArray(n),0.0000000000000000001)
    val res = DoubleArray(n)
    for (i in 0 until n) {
        res[i] = un(i.toDouble() / n.toDouble(), u, n)
    }
    println("----------------")
    println("pohubka")
    val ytochne = DoubleArray(n)
    for (i in 0 until n) {
        ytochne[i] = yTochne(i.toDouble() / n.toDouble())
    }
    println((getPohubka(ytochne,res)))


}