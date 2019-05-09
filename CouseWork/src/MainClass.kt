
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

//    val n = 8
//    var s = Supermatrix()
//    val c = ClusterTree.buildClusterTree(n)
//    try {
//        s = BlockClusterTree.buildBlockClusterTree(c, c, s, n)
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
 //   println("u_approx")
//    val u = gradientMethodMatrix(s, f, x0)
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
//    val temp2 = BlockClusterTree.MultHMatrixByVector(s, doubleArrayOf(1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0))
//    temp2.forEach { println(it) }
      val m= 8
     val superm = Supermatrix()
    val cl = ClusterTree.buildClusterTree(m)
    val sppp = BlockClusterTree.buildBlockClusterTree(cl, cl, superm, m)
    val res = BlockClusterTree.MultHMatrixByVector(sppp, doubleArrayOf(1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0))
    res.forEach { println(it) }
    println("==============")
    val per = gradientMethodMatrix(sppp, res, doubleArrayOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0))
    per.forEach { println(it) }
//    val s = ClusterTree.buildClusterTree(8)
//    val t = ClusterTree.buildClusterTree(8)
//    val tree:ClusterTree? = ClusterTree.getTreeByIndex(ClusterTree.buildClusterTree(8),3,0)
//    tree?.leaf?.forEach { println(it) }
//
//    println(BlockClusterTree.isAdmissible(s.leftTree!!.leftTree!!,t.rightTree!!.rightTree!!))
//
//    val res = gradientMethod(arrayOf(doubleArrayOf(4.0,1.0), doubleArrayOf(1.0,3.0)), doubleArrayOf(1.0,2.0),doubleArrayOf(2.0,1.0))
//    res.forEach { println(it) }
//    val rkmatrix = BlockClusterTree.buildRkmatrix(t,s,8)
//    println(multiplyVectorByVector(doubleArrayOf(1.0,2.0), doubleArrayOf(5.0,6.0)))
}