package src.secondpr


fun gamma(y: Double, pStart: Pair<Double, Double>, pEnd: Pair<Double, Double>):Pair<Double, Double>{
    return Pair(pStart.first*(1-y)+pEnd.first*y,pStart.second*(1-y)+pEnd.second*y)
}
fun getGamma(x: Double, i: Int):Pair<Double, Double>{
    if (i == points.size){
        return gamma(x, points[i-1], points[0])
    }
    return gamma(x, points[i-1], points[i])
}