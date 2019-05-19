
fun f(x:Double):Double{
    if ((x-1)<0||x<0){
        return (1-x)*Math.log(1-x) -(1-x)+x*Math.log(-x)-x
    }else
        return -((x-1)*Math.log(x-1)-(x-1))+(x*Math.log(x)-x)
}

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

fun main(args : Array<String>) {

//    val n = 4
//    var s = Supermatrix()
//    val c = ClusterTree.buildClusterTree(n)
//    try {
//        s = BlockClusterTree.buildBlockClusterTree(c, c, s, n,1)
//    } catch (e: Exception) {
//        println("error")
//    }
//
//    val f = DoubleArray(n)
//    val x0 = DoubleArray(n){0.0}
//    for (i in 0 until n) {
//        f[i] = f(i.toDouble() / n.toDouble())
//    }
//
//    println("----------------")
//    println("u_approx")
//    val u = solveMatrix(0.0000001,s, x0, f)
//    for (i in 0 until n) {
//        println(un(i.toDouble() / n.toDouble(), u, n))
//    }
//    println("----------------")
//    println("u_tochne")
//    val ytochne = DoubleArray(n)
//    for (i in 0 until n) {
//        ytochne[i] = yTochne(i.toDouble() / n.toDouble())
//        println(yTochne(i.toDouble() / n.toDouble()))
//    }
    //TODO testing
      val m= 4
    val nmin =m
     val superm = Supermatrix()
    val cl = ClusterTree.buildClusterTree(m,nmin)
    val sppp = BlockClusterTree.buildBlockClusterTree(cl, cl, superm, m,nmin)
    val ourArray = DoubleArray(m){
        it.toDouble()
    }
    val x0 = DoubleArray(m){0.0}
    val testMatrix =BlockClusterTree.getNormalMatrix(sppp)
    for (i in 0 until testMatrix.size){
        for (j in 0 until testMatrix[i].size){
            print(testMatrix[i][j].toString()+" ")
        }
        println()
    }
    val res = BlockClusterTree.MultHMatrixByVector(sppp, ourArray)
    res.forEach { println(it) }
    println("==============")
    println("solve")
    val superm2 = Supermatrix()
    val cl2 = ClusterTree.buildClusterTree(m,nmin)
    val sppp2 = BlockClusterTree.buildBlockClusterTree(cl2, cl2, superm2, m,nmin)
    val per = solveMatrix(0.0001,sppp2,DoubleArray(m),res)
    per.forEach { println(it) }

    println("=============")
    println(getPohubka(ourArray,per))
    println("===============")
    val resttt = multiplyMatrixByVector(testMatrix,ourArray)
    val per2 =solve(0.0001,testMatrix, DoubleArray(m),resttt)
    println("=============")
    println(getPohubka(ourArray,per2))
    println("===============")

}