package src.secondpr

import getPohubka
import gradientMethodMatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree

fun f(x: Pair<Double, Double>): Double{
    return 2.0
}

fun f2(x: Pair<Double, Double>): Double {
    return (x.first+x.second)/(Math.pow(x.first,2.0)+Math.pow(x.second,2.0))
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
    val n = 1024
    val m = 2
    val nmin = (m+1)*(m+1)
    val clusterTree = buildClusterTree(n, m, nmin,func1 = {t:Double-> Math.cos(t)+1 }, func2 = { t:Double-> Math.sin(t)+1 })
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(sLeaf = clusterTree.leaf, tLeaf = clusterTree.leaf),n, nmin)
    val f = DoubleArray(n)
    for (i in 1 until n+1) {
        if (i == points.size){
            f[i-1] = norm(Pair(points[0].first - points[i-1].first, points[0].second - points[i-1].second))* Legendre(6).integrateF(::f2,i)
        } else
            f[i-1] = norm(Pair(points[i].first - points[i-1].first, points[i].second - points[i-1].second))* Legendre(6).integrateF(::f2,i)
    }
    val u = gradientMethodMatrix((sprm),f, DoubleArray(n),0.000001)
    val appr = DoubleArray(5)
    val tochne = DoubleArray(5)
    getTestPoints2(5).forEachIndexed { index, pair ->
        appr[index] = calculateU(pair, u)
        tochne[index] = f2(pair)
    }
    println(getPohubka(appr, tochne))
//    println("----------")
//    appr.forEach { println(it)}
//    println("----------")
//    tochne.forEach { println(it) }
}

fun getTestPoints(n:Int): List<Pair<Double, Double>>{
    return listOf(Pair(-0.5, 0.5), Pair(-0.25,0.25), Pair(0.0, 0.0), Pair(0.25, -0.25), Pair(0.5,-0.5))
}
fun getTestPoints2(n:Int): List<Pair<Double, Double>>{
    return listOf(Pair(0.25,0.5), Pair(0.5,0.5), Pair(0.75, 0.75), Pair(0.6, 0.5), Pair(0.5,0.75))
}