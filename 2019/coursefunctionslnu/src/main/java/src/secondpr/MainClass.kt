package src.secondpr

import gradientMethodMatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree

fun f(x: Pair<Double, Double>): Double{
    return 1.0
}

fun main(){
    val n = 4
    val k = 1
    val clusterTree = buildClusterTree(n,k)
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(),n,k)
    println("------------------")
    val f = DoubleArray(2*n)
    for (i in 1 until 2*n) {
        f[i-1] = norm(Pair(points[i].first - points[i-1].first, points[i].second - points[i-1].second))* Legendre(6).integrateF(::f,i)
        println(f[i-1])
    }
    val u = gradientMethodMatrix((sprm),f, DoubleArray(2*n),0.0000000000000000001)
    println("------------------")
    u.forEach { println(it) }
}