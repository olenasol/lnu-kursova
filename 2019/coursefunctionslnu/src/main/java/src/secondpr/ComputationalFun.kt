package src.secondpr

import Egtulda01
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sqrt

lateinit var points: Array<Pair<Double,Double>>
lateinit var segments: List<Segment>

private fun getPoints(n:Int, func1:(x:Double)->Double,func2:(x:Double)->Double): Array<Pair<Double,Double>>{
    val points = DoubleArray(n) { j -> (2*j*Math.PI)/n}
    src.secondpr.points = Array(points.size){i-> Pair(0.0,0.0)}
    for (i in points.indices){
        src.secondpr.points[i] = Pair(func1(points[i]), func2(points[i]))
    }
    return src.secondpr.points
}
fun buildBoundaryElements(n:Int = 10,func1:(x:Double)->Double,func2:(x:Double)->Double):List<Int>{
    getPoints(n,func1,func2)
    val points = DoubleArray(n) { j -> (2*j*Math.PI)/n}
    val list = mutableListOf<Segment>()
    val indexes = mutableListOf<Int>()
    for (i in 0 until (points.size-1)){
        list.add(Segment(Pair(func1(points[i]), func2(points[i])), Pair(func1(points[i+1]), func2(points[i+1]))))
        indexes.add(i)
    }
    indexes.add(n-1)
    list.add(Segment(Pair(func1(points[points.size-1]), func2(points[points.size-1])), Pair(func1(points[0]), func2(points[0]))))
    segments = list
    return indexes.toList()
}
private fun maxX(list:List<Pair<Double,Double>>):Double{
    var max = list[0].first
    if (list.size > 1)
        for (i in 1 until list.size){
            if (list[i].first > max)
                max = list[i].first
        }
    return max
}
private fun maxY(list:List<Pair<Double,Double>>):Double{
    var max = list[0].second
    if (list.size > 1)
        for (i in 1 until list.size){
            if (list[i].second > max)
                max = list[i].second
        }
    return max
}
private fun minX(list:List<Pair<Double,Double>>):Double{
    var min = list[0].first
    if (list.size > 1)
        for (i in 1 until list.size){
            if (list[i].first < min)
                min = list[i].first
        }
    return min
}
private fun minY(list:List<Pair<Double,Double>>):Double{
    var min = list[0].second
    if (list.size > 1)
        for (i in 1 until list.size){
            if (list[i].second < min)
                min = list[i].second
        }
    return min
}


fun findBoundingBox(list:List<Pair<Double,Double>>):List<Pair<Double,Double>>{
    return listOf(Pair(maxX(list),maxY(list)), Pair(maxX(list), minY(list)),
            Pair(minX(list), minY(list)), Pair(minX(list), maxY(list)))
}

fun calculateDiamOfBoundingBox(boundingBox: List<Pair<Double,Double>>): Double {
    return sqrt(((boundingBox[0].first-boundingBox[2].first)*(boundingBox[0].first-boundingBox[2].first)+
            (boundingBox[0].second-boundingBox[2].second)*(boundingBox[0].second-boundingBox[2].second)).toDouble())
}
fun getSplitOnSegment01(m: Int) : Array<Double>{
    return  Array(m+1){
        v -> cos(((2.0*v+1.0)/(2.0*m+2.0))*Math.PI)
    }
}
fun getSplitOnSegmentab(list: List<Int>, m: Int): List<Pair<Double,Double>>{
    val array01 = getSplitOnSegment01(m)
    val tempList = mutableListOf<Pair<Double,Double>>()
    segments.slice(list).forEach {
        tempList.add(it.startPoint)
        tempList.add(it.endPoint)
    }
    val boundingBox = findBoundingBox(tempList)
    val bmin = arrayListOf(boundingBox[2].first, boundingBox[2].second)
    val bmax = arrayListOf(boundingBox[0].first, boundingBox[0].second)
    val mid1 = 0.5 * (bmax[0]+ bmin[0])
    val dif1 = 0.5 * (bmax[0]- bmin[0])
    val mid2 = 0.5 * (bmax[1]+ bmin[1])
    val dif2 = 0.5 * (bmax[1]- bmin[1])
    val result = mutableListOf<Pair<Double, Double>>()
    for (i in 0 until (m+1)){
        result.add(Pair(mid1 + array01[i] * dif1, mid2 + array01[i] * dif2))
    }
    return result
}

fun getLogIntegral(y: Double, x_v: Pair<Double, Double>, j: Int): Double{
    val g = getGamma(y, j)
    return Math.log(norm(Pair(x_v.first-g.first, x_v.second - g.second)))
}
fun getLogIntegralDouble(x: Double, y: Double, i:Int, j: Int): Double{
    val g1 = getGamma(x,i)
    val g2 = getGamma(y, j)
    return Math.log(norm(Pair(g1.first-g2.first, g1.second - g2.second)))
}

fun getLagrangeMultiplied(y:Double, j:Int,nu: Pair<Int,Int>,l:List<Pair<Double,Double>>, m:Int): Double{
    val x = getGamma(y, j)
    var result = 1.0
        for(i in 0 until (m+1)) {
            if (i != nu.toList()[0])
                result *= (x.first - l[i].first) / (l[nu.toList()[0]].first - l[i].first)
            if (i != nu.toList()[1]){
                result *= (x.second - l[i].second) / (l[nu.toList()[1]].second - l[i].second)
            }
        }
    return result
}

fun norm(x: Pair<Double, Double>): Double{
    return sqrt(x.first*x.first + x.second*x.second)
}

fun calculateDistanceBetweenBoundingBoxes(firstbb: List<Pair<Double,Double>>, secondbb: List<Pair<Double,Double>>): Double{
    val distances = mutableListOf<Double>()
    for (segmentFromFirst in getListOfSegments(firstbb)){
        for (segmentFromSecond in getListOfSegments(secondbb)){
            distances.add(calculateDistanceBetweenTwoSides(segmentFromFirst, segmentFromSecond))
        }
    }
    var min = Double.MAX_VALUE
    for (dist in distances){
        if (dist < min)
            min = dist
    }
    return min
}
private fun getListOfSegments(boundingBox: List<Pair<Double,Double>>): List<Segment>{
    val segments = mutableListOf<Segment>()
    segments.add(Segment(boundingBox[0],boundingBox[1]))
    segments.add(Segment(boundingBox[1], boundingBox[2]))
    segments.add( Segment(boundingBox[2],boundingBox[3]))
    segments.add(Segment(boundingBox[3], boundingBox[0]))
    return segments
}

fun calculateDistanceBetweenTwoSides(firstSegment: Segment, secondSegment: Segment): Double{
    val projectedXFirstSegment = Segment(Pair(firstSegment.startPoint.first,0.0), Pair(firstSegment.endPoint.first,0.0))
    val projectedXSecondSegment = Segment(Pair(secondSegment.startPoint.first,0.0), Pair(secondSegment.endPoint.first,0.0))
    var dx =0.0
    if (!isXIntersect(projectedXFirstSegment,projectedXSecondSegment))
        dx = projectedXSecondSegment.startPoint.first - projectedXFirstSegment.endPoint.first
    val projectedYFirstSegment = Segment(Pair(0.0,firstSegment.startPoint.second), Pair(0.0, firstSegment.endPoint.second))
    val projectedYSecondSegment = Segment(Pair(0.0, secondSegment.startPoint.second), Pair(0.0,secondSegment.endPoint.second))
    var dy = 0.0
    if (!isYIntersect(projectedYFirstSegment,projectedYSecondSegment))
       dy = projectedYSecondSegment.startPoint.second - projectedYFirstSegment.endPoint.second
    return sqrt(dx*dx + dy*dy)
}
fun isYIntersect(projectedFirst: Segment, projectedSecond: Segment): Boolean{
    return ((projectedFirst.startPoint.second>= projectedSecond.startPoint.second &&
                    projectedFirst.endPoint.second<= projectedSecond.endPoint.second) ||
            (projectedSecond.startPoint.second>= projectedFirst.startPoint.second &&
                    projectedSecond.endPoint.second<= projectedFirst.endPoint.second))
}
fun isXIntersect(projectedFirst: Segment, projectedSecond: Segment): Boolean{
    return ((projectedFirst.startPoint.first>= projectedSecond.startPoint.first &&
            projectedFirst.endPoint.first<= projectedSecond.endPoint.first) ||
            (projectedSecond.startPoint.first>= projectedFirst.startPoint.first &&
                    projectedSecond.endPoint.first<= projectedFirst.endPoint.first))
}
fun splitBoundingBox(list:List<Int>):Pair<List<Int>,List<Int>>{
    val tempList = mutableListOf<Pair<Double,Double>>()
    val convertedList = segments.slice(list)
    convertedList.forEach {
        tempList.add(it.startPoint)
        tempList.add(it.endPoint)
    }
    val boundingBox = findBoundingBox(tempList)
    val distanceHor = boundingBox[0].first - boundingBox[2].first
    val distanceVer = boundingBox[0].second - boundingBox[2].second
    val isVertical = distanceHor > distanceVer
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()
    val halfRec = if (isVertical){
        mutableListOf(boundingBox[0], boundingBox[1],
                Pair((boundingBox[2].first+boundingBox[1].first)/2.0,boundingBox[1].second),
                Pair((boundingBox[3].first+boundingBox[0].first)/2.0,boundingBox[0].second))
    } else
        mutableListOf(boundingBox[0],
                Pair(boundingBox[0].first,(boundingBox[0].second+boundingBox[1].second)/2.0),
                Pair(boundingBox[3].first,(boundingBox[3].second+boundingBox[2].second)/2.0),
                boundingBox[3])
    val isFirstPointInRectangle = isPointInRectangle((Pair((convertedList[0].startPoint.first+convertedList[0].endPoint.first)/2.0,
            (convertedList[0].startPoint.second+convertedList[0].endPoint.second)/2.0)),halfRec)
    list.indices.forEach {i ->
        if (isPointInRectangle((Pair((convertedList[i].startPoint.first+convertedList[i].endPoint.first)/2.0,
                        (convertedList[i].startPoint.second+convertedList[i].endPoint.second)/2.0)),halfRec) )
            firstList.add(list[i])
        else
            secondList.add(list[i])
    }
    return if(isFirstPointInRectangle){
         Pair(firstList.toList(),secondList.toList())
    } else{
         Pair(secondList.toList(), firstList.toList())
    }
}
private fun isPointInRectangle(point:Pair<Double,Double>, rectangle: List<Pair<Double,Double>>):Boolean{
    return ((point.first>=rectangle[2].first && point.first<=rectangle[1].first) &&
            (point.second>=rectangle[1].second && point.second<=rectangle[0].second))
}