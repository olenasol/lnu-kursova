package src.secondpr

import java.lang.Math.sqrt

fun gamma(y: Double, pStart: Pair<Double, Double>, pEnd: Pair<Double, Double>):Pair<Double, Double>{
    return Pair(pStart.first*(1-y)+pEnd.first*y,pStart.second*(1-y)+pEnd.second*y)
}
fun getGamma(x: Double, i: Int):Pair<Double, Double>{
    return gamma(x, points[i-1], points[i])
}

private fun isPointOnSegment(p:Pair<Double,Double>, a: Pair<Double, Double>, b: Pair<Double,Double>): Boolean{
    val AB = round(sqrt((b.first-a.first)*(b.first-a.first)+(b.second-a.second)*(b.second-a.second)))
    val AP = round(sqrt((p.first-a.first)*(p.first-a.first)+(p.second-a.second)*(p.second-a.second)))
    val PB = round(sqrt((b.first-p.first)*(b.first-p.first)+(b.second-p.second)*(b.second-p.second)))
    return AB == AP + PB
}

private fun round(value:Double):Double{
    return  Math.round(value * 100000.0) / 100000.0
}

fun getPhi(x:Pair<Double,Double>,i:Int): Double{
    return if (isPointOnSegment(x, points[i-1], points[i])){
        1.0
    } else 0.0
}