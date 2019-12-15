package src.secondpr

import Egtulda01
import getPohubka
import gradientMethod
import gradientMethodMatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree
import src.secondpr.integral.Legendre

fun f(x: Pair<Double, Double>): Double{
    return 1.0
}

fun f2(x: Pair<Double, Double>): Double {
    return (x.first+x.second)/(Math.pow(x.first,2.0)+Math.pow(x.second,2.0))
}

fun f3(x:Pair<Double, Double>): Double {
    val xZirka = Pair(2.0,2.0)
    return Math.log(norm(Pair(x.first - xZirka.first, x.second-xZirka.second)))
}

fun calculateU(x:Pair<Double, Double>, u:DoubleArray ): Double{
    var sum = 0.0
    for (i in 1 until u.size+1){
        if (i == points.size){
            sum += -(1.0 / (2.0 * Math.PI)) * u[i - 1] * norm(Pair(points[0].first - points[i - 1].first,
                    points[0].second - points[i - 1].second)) * Legendre().integrateLog(x, i)
        } else {
            sum += -(1.0 / (2.0 * Math.PI)) * u[i - 1] * norm(Pair(points[i].first - points[i - 1].first,
                    points[i].second - points[i - 1].second)) * Legendre().integrateLog(x, i)
        }
    }
    return sum
}

fun main(){
    val startTime = System.nanoTime()
    val n = 4096
    val m = 4
    val nmin = (m+1)*(m+1)
    val clusterTree = buildClusterTree(n, m, nmin,func1 = {t:Double-> Math.cos(t+10)}, func2 = { t:Double-> Math.sin(t) })
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(sLeaf = clusterTree.leaf, tLeaf = clusterTree.leaf),n, nmin)
    val testMatrix = calculateTochno(n)
    val f = DoubleArray(n)
    for (i in 1 until n+1) {
        if (i == points.size){
            f[i-1] = norm(Pair(points[0].first - points[i-1].first, points[0].second - points[i-1].second))* Legendre().integrateF(::f3,i)
        } else
            f[i-1] = norm(Pair(points[i].first - points[i-1].first, points[i].second - points[i-1].second))* Legendre().integrateF(::f3,i)
    }
    val u = gradientMethodMatrix((sprm),f, DoubleArray(n),0.0000000001)
    //val u = gaussPartial(testMatrix, f)//gradientMethod(testMatrix,f, DoubleArray(n),0.000001)
    val appr = DoubleArray(5)
    val tochne = DoubleArray(5)
    getTestPoints3(5).forEachIndexed { index, pair ->
        appr[index] = calculateU(pair, u)
        tochne[index] = f3(pair)
    }
    println(getPohubka(appr, tochne))
    val elapsedTime = System.nanoTime() - startTime;

    println("Total execution time to create 1000K objects in Java in millis: "
            + elapsedTime/1000000);

}

fun getTestPoints(n:Int): List<Pair<Double, Double>>{
    val step = 1.0 / n.toDouble()
    val points = mutableListOf<Pair<Double, Double>>()
    for (i in 0 until (n)){
        points.add(Pair(-0.5 + i*step,-0.25 + i*(step/4.0)))
    }
    return points
    //return listOf(Pair(-0.5, 0.5), Pair(-0.25,0.25), Pair(0.0, 0.0), Pair(0.25, -0.25), Pair(0.5,-0.5))
}
fun getTestPoints2(n:Int): List<Pair<Double, Double>>{
    return listOf(Pair(0.75,0.5), Pair(0.5,0.5), Pair(0.75, 0.75), Pair(1.0, 1.0), Pair(0.5,0.75))
}
fun getTestPoints3(n:Int): List<Pair<Double, Double>>{
    return listOf(Pair(0.0,0.5), Pair(0.5,0.5), Pair(-0.5, 0.0), Pair(0.5, 0.0), Pair(-0.5,-0.5))
}
fun calculateTochno(n:Int):Array<DoubleArray>{
    val test1 =  Array(n){
        DoubleArray(n)
    }
    for (i in 1 until test1.size+1){
        for (j in 1 until test1[i-1].size+1){
            //rint(testMatrix[i][j].toString()+" ")
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
                Legendre().integrateLogDouble(i,j)
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
    return test1
}