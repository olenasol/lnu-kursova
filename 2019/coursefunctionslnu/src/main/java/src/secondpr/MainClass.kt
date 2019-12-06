package src.secondpr

import Egtulda01
import gradientMethodMatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree

fun f(x: Pair<Double, Double>): Double{
    return 1.0
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
    val n = 64
    val m = 2
    val nmin = (m+1)*(m+1)
    val clusterTree = buildClusterTree(n,m,nmin)
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(),0,0,n,nmin.toLong())
    val testMatrix = BlockClusterTree.getNormalMatrix(sprm)
    var test1 =  Array(n){
        DoubleArray(n)
    }
    for (i in 1 until test1.size+1){
        for (j in 1 until test1[i-1].size+1){
            var integral =0.0
            integral = if (i==j){
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
    println("tochna")
    for (i in 0 until test1.size) {
        for (j in 0 until test1[i].size) {
            print(test1[i][j].toString()+" ")
        }
        println()
    }
    println("------------------")
    println("approximate")
    for (i in 0 until testMatrix.size) {
        for (j in 0 until testMatrix[i].size) {
            print(testMatrix[i][j].toString()+" ")
        }
        println()
    }
    println("------------------")
    val martrix = BlockClusterTree.getNormalMatrix(sprm)
        for (i in 0 until martrix.size) {
        for (j in 0 until martrix[i].size) {
            print(martrix[i][j].toString()+" ")
        }
        println()
    }
    println("------------------")
    val f = DoubleArray(n)
    for (i in 1 until n+1) {
        if (i == points.size){
            f[i-1] = norm(Pair(points[0].first - points[i-1].first, points[0].second - points[i-1].second))* Legendre(6).integrateF(::f,i)
        } else
            f[i-1] = norm(Pair(points[i].first - points[i-1].first, points[i].second - points[i-1].second))* Legendre(6).integrateF(::f,i)
        println(f[i-1])
    }
    val u = gradientMethodMatrix((sprm),f, DoubleArray(n),0.1)
    //val u = gradientMethod(test1,f, DoubleArray(n),0.000001)
    println("------------------")
    u.forEach { println(it) }
    val boundaryElements = buildBoundaryElements(n=n,func1 = {t:Double-> Math.cos(t) }, func2 = { t:Double-> Math.sin(t) })
    val list = mutableListOf<Pair<Double, Double>>()
    boundaryElements.forEach { list.add(Pair((it.startPoint.first+it.endPoint.first)/2.0,(it.startPoint.second+it.endPoint.second)/2.0)) }
    println("------------------")
    println(calculateU(Pair(0.0,0.25), u))
}

fun testMatrixMult(){
    val sp =Supermatrix()
    val sp1 = Supermatrix()
    sp1.blockcols = 0
    sp1.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0)))
    val sp2 = Supermatrix()
    sp2.blockcols = 1
    sp2.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0,2.0,3.0), doubleArrayOf(4.0,5.0,6.0), doubleArrayOf(4.0,3.0,2.0)))
    val sp3 = Supermatrix()
    sp2.blockcols = 0
    sp3.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0), doubleArrayOf(4.0), doubleArrayOf(4.0)))
    val sp4 = Supermatrix()
    sp4.blockcols = 1
    sp4.fullmatrix = Fullmatrix(e= arrayOf(doubleArrayOf(1.0,2.0,3.0)))
    sp.supermatrix = arrayOf(arrayOf(sp1,sp2), arrayOf(sp3,sp4) )

    var doubleArray = doubleArrayOf(1.0,2.0,3.0,4.0)
    BlockClusterTree.MultHMatrixByVector(sp,doubleArray,0)
}