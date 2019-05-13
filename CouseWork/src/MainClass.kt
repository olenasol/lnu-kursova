
import sun.rmi.runtime.Log
import java.io.Console



fun f(x: Double): Double {
    return 1.0
}

fun f_i(x: Double, i: Int, n: Int): Double {
    return f(x)//FunctionsBEM.phi(i, n, x);
}

fun un(x: Double, u: DoubleArray, n: Int): Double {
    var s = 0.0
    for (i in 0 until n) {

        s +=  phi(i, n, x) * u[i]
    }
    return s
}

fun yTochne(x: Double): Double {
    return 1.0 / (Math.pow(Math.PI, 2.0) * Math.log(0.25) * Math.sqrt(x * (1 - x)))
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
//    val x0 = DoubleArray(n)
//    for (i in 0 until n) {
//        f[i] = f_i(i.toDouble() / n.toDouble(), i, n)
//        x0[i] = 0.0
//    }
//    println("----------------")
//    println("testing")
//    val test = solve(0.00001,BlockClusterTree.getNormalMatrix(s),x0,f)
//    test.forEach { println(it) }
//    println("u_approx")
//    val u = solveMatrix(0.00001,s, x0, f)
//    for (i in 0 until n) {
//        println(un(i.toDouble() / n.toDouble(), u, n))
//    }
//    println("----------------")
//    println("u_tochne")
//    val tst = BlockClusterTree.getNormalMatrix(s)
//    for (i in 0 until tst.size){
//        for (j in 0 until tst[i].size){
//            print(" "+tst[i][j])
//        }
//        println()
//    }
//    val ytochne = DoubleArray(n)
//    for (i in 0 until n) {
//        ytochne[i] = yTochne(i.toDouble() / n.toDouble())
//        println(yTochne(i.toDouble() / n.toDouble()))
//    }
    //TODO testing
      val m= 8
     val superm = Supermatrix()
    val cl = ClusterTree.buildClusterTree(m)
    val sppp = BlockClusterTree.buildBlockClusterTree(cl, cl, superm, m,1)
    val ourArray = DoubleArray(m){
        it.toDouble()
    }
    val x0 = DoubleArray(m){0.0}
    val res = BlockClusterTree.MultHMatrixByVector(sppp, ourArray)
    res.forEach { println(it) }
    println("==============")
//    println("solve")
//    val per = solveMatrix(0.000000001,sppp,x0,res)
//    per.forEach { println(it) }

    println("=============")
    val test = BlockClusterTree.getNormalMatrix(sppp)
    for (i in 0 until test.size){
        for (j in 0 until test[i].size){
            print(" "+"%.8f".format(test[i][j]))
        }
        println()
    }
    val t = multiplyMatrixByVector(test,ourArray)
    val p = solve(0.0000001,test,x0, t)
    p.forEach { println(it) }
    println("===============")
    println(amatr(6,8,0.9375,1))
}