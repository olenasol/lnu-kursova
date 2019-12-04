package src.secondpr

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
    val n = 48
    val k = 1
    val clusterTree = buildClusterTree(n,k)
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(),0,0,n,k)
    //val testMatrix = BlockClusterTree.getNormalMatrix(sprm)
//    var test1 =  Array(n){
//        DoubleArray(n)
//    }
//    for (i in 1 until test1.size+1){
//        for (j in 1 until test1[i-1].size+1){
//            var integral =0.0
//            integral = if (i==j){
//                if (i == points.size){
//                    Egtulda01() + Math.log(norm(Pair(points[0].first - points[i - 1].first,
//                            points[0].second - points[i - 1].second)))
//                } else {
//                    Egtulda01() + Math.log(norm(Pair(points[i].first - points[i - 1].first,
//                            points[i].second - points[i - 1].second)))
//                }
//            } else{
//                Legendre(6).integrateLogDouble(i,j)
//            }
//            if (i == points.size && j == points.size){
//                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
//                        points[0].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
//                        points[0].second - points[i - 1].second)) * integral
//            } else if (i == points.size){
//                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
//                        points[j].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
//                        points[0].second - points[i - 1].second)) * integral
//            } else if (j == points.size){
//                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
//                        points[0].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
//                        points[i].second - points[i - 1].second)) * integral
//            } else
//                test1[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
//                    points[j].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
//                    points[i].second - points[i - 1].second)) * integral
//        }
//    }
//    for (i in 0 until test1.size) {
//        for (j in 0 until test1[i].size) {
//            print(test1[i][j].toString()+" ")
//        }
//        println()
//    }
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
    val u = gradientMethodMatrix((sprm),f, DoubleArray(2*n),0.1)
    //val u = gradientMethod(test1,f, DoubleArray(n),0.000001)
    println("------------------")
    u.forEach { println(it) }
    val boundaryElements = buildBoundaryElements(n=n,func1 = {t:Double-> Math.cos(t) }, func2 = { t:Double-> Math.sin(t) })
    val list = mutableListOf<Pair<Double, Double>>()
    boundaryElements.forEach { list.add(Pair((it.startPoint.first+it.endPoint.first)/2.0,(it.startPoint.second+it.endPoint.second)/2.0)) }
    println("------------------")
    println(calculateU(Pair(0.0,0.25), u))


}