package src.secondpr

import Egtulda01
import gradientMethodMatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree

fun f(x: Pair<Double, Double>): Double{
    return 2.0
}
fun calculateU(x:Pair<Double, Double>, u:DoubleArray ): Double{
    var sum = 0.0
    for (i in 1 until u.size+1){
        if (i == points.size){
            sum += -(1.0 / (2.0 * Math.PI)) * u[i - 1] * norm(Pair(points[0].first - points[i - 1].first,
                    points[0].second - points[i - 1].second)) * Legendre(6).integrateLog(x, i)
        } else {
            sum += -(1.0 / (2.0 * Math.PI)) * u[i - 1] * norm(Pair(points[i].first - points[i - 1].first,
                    points[i].second - points[i - 1].second)) * Legendre(6).integrateLog(x, i)
        }
    }
    return sum
}

fun main(){
    val n = 16
    val m = 2
    val nmin = (m+1)*(m+1)
    val clusterTree = buildClusterTree(n, m, nmin)
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(sLeaf = clusterTree.leaf, tLeaf = clusterTree.leaf),n, nmin)
    val testMatrix = BlockClusterTree.getNormalMatrix(sprm)
    val test1 =  Array(n){
        DoubleArray(n)
    }
    println("==============")
    println("exact")
    for (i in 1 until test1.size+1){
        for (j in 1 until test1[i-1].size+1){
            val integral = if (i==j){
                if (i == points.size){
                    Egtulda01() + Math.log(norm(Pair(points[0].first - points[i - 1].first,
                            points[0].second - points[i - 1].second)))
                } else {
                    Egtulda01() + Math.log(norm(Pair(points[i].first - points[i - 1].first,
                            points[i].second - points[i - 1].second)))
                }
            } else{
                Legendre(6).integrateLogDouble(i,j)
            }
            if (i == points.size && j == points.size){
                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
                        points[0].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
                        points[0].second - points[i - 1].second)) * integral
            } else if (i == points.size){
                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
                        points[j].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
                        points[0].second - points[i - 1].second)) * integral
            } else if (j == points.size){
                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
                        points[0].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
                        points[i].second - points[i - 1].second)) * integral
            } else
                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
                    points[j].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
                    points[i].second - points[i - 1].second)) * integral
        }
    }
//    println("tochna")
//    for (i in test1.indices) {
//        for (element in test1[i]) {
//            print("$element ")
//        }
//        println()
//    }
//    println("------------------")
//    println("approximate")
//    for (i in testMatrix.indices) {
//        for (element in testMatrix[i]) {
//            print("$element ")
//        }
//        println()
//    }
//    println("------------------")
    val f = DoubleArray(n)
    for (i in 1 until n+1) {
        if (i == points.size){
            f[i-1] = norm(Pair(points[0].first - points[i-1].first, points[0].second - points[i-1].second))* Legendre(6).integrateF(::f,i)
        } else
            f[i-1] = norm(Pair(points[i].first - points[i-1].first, points[i].second - points[i-1].second))* Legendre(6).integrateF(::f,i)
    }
    val u = gradientMethodMatrix((sprm),f, DoubleArray(n),0.000001)
    getTestPoints().forEach{
        println(calculateU(it, u))
    }
}

fun getTestPoints(): List<Pair<Double, Double>>{
    return listOf(Pair(-0.5, 0.5), Pair(-0.25,0.25), Pair(0.0, 0.0), Pair(0.25, -0.25), Pair(0.5,-0.5))
}

//fun testMatrixMult(){
//    val sp =Supermatrix()
//    val sp1 = Supermatrix()
//    sp1.blockcols = 0
//    sp1.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0)))
//    val sp2 = Supermatrix()
//    sp2.blockcols = 1
//    sp2.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0,2.0,3.0), doubleArrayOf(4.0,5.0,6.0), doubleArrayOf(4.0,3.0,2.0)))
//    val sp3 = Supermatrix()
//    sp2.blockcols = 0
//    sp3.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0), doubleArrayOf(4.0), doubleArrayOf(4.0)))
//    val sp4 = Supermatrix()
//    sp4.blockcols = 1
//    sp4.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0,2.0,3.0)))
//    sp.supermatrix = arrayOf(arrayOf(sp1,sp2), arrayOf(sp3,sp4) )
//
//    var doubleArray = doubleArrayOf(1.0,2.0,3.0,4.0)
//    BlockClusterTree.MultHMatrixByVector(sp,doubleArray,0)
//}