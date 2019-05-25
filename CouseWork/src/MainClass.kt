
fun f(i:Int,n:Int,x:Double):Double{
//    var sum = 0.0
//    if(x!=0.0)
//        sum+=x*Math.log(Math.abs(x))
//    if((x-1)!=0.0)
//        sum+=-Math.log(Math.abs(x-1))*x+Math.log(Math.abs(x-1))
//    sum+=-1

    return -3.0*phi(i,n,x)
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

    val n = 8
    val nmin = 1
    var s = Supermatrix()
    val c = ClusterTree.buildClusterTree(n,nmin)
    try {
        s = BlockClusterTree.buildBlockClusterTree(c, c, s, n,nmin)
    } catch (e: Exception) {
        println("error")
    }
    val testMatrix =BlockClusterTree.getNormalMatrix(s)
    for (i in 0 until testMatrix.size){
        for (j in 0 until testMatrix[i].size){
           // testMatrix[i][i] = 1.0
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
        println(res[i])
    }
    println("----------------")
    println("pohubka")
    val ytochne = DoubleArray(n)
    for (i in 0 until n) {
        ytochne[i] = yTochne(i.toDouble() / n.toDouble())
    }
    println(getPohubka(ytochne,res))
    //TODO testing
//      val m= 256
//    val nmin = 8
//     val superm = Supermatrix()
//    val cl = ClusterTree.buildClusterTree(m,nmin)
//    val sppp = BlockClusterTree.buildBlockClusterTree(cl, cl, superm, m,nmin)
//    val ourArray = DoubleArray(m){
//        it.toDouble()
//    }
//    val x0 = DoubleArray(m){0.0}
//    val testMatrix =BlockClusterTree.getNormalMatrix(sppp)
//    for (i in 0 until testMatrix.size){
//        for (j in 0 until testMatrix[i].size){
//           // testMatrix[i][i] = 1.0
//            print(testMatrix[i][j].toString()+" ")
//
//        }
//        println()
//    }
//    println("=================")
//    val res = BlockClusterTree.MultHMatrixByVector(sppp, ourArray)
//    res.forEach { println(it) }
//    println("==============")
//    println("solve")
//    val superm2 = Supermatrix()
//    val cl2 = ClusterTree.buildClusterTree(m,nmin)
//    val sppp2 = BlockClusterTree.buildBlockClusterTree(cl2, cl2, superm2, m,nmin)
//    val per = gradientMethod(BlockClusterTree.getNormalMatrix(sppp2),res,DoubleArray(m),0.00000000000000001)
//    per.forEach { println(it) }
//
//    println("=============")
//    println(getPohubka(ourArray,per))
//    println("===============")
//    val resttt = multiplyMatrixByVector(testMatrix,ourArray)
//    val per2 =BlockClusterTree.MetodHausa(m,testMatrix,resttt)
//    println("=============")
//    println(getPohubka(ourArray,per2))
//    println("===============")

}