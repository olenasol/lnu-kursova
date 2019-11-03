package src.secondpr

import com.google.common.math.IntMath.pow
import java.awt.Rectangle
import kotlin.math.abs
import kotlin.math.sqrt

fun buildBoundaryElements(n:Int = 10,func1:(x:Double)->Double,func2:(x:Double)->Double):List<Segment>{
    val points = DoubleArray(2*n, { j -> (j*Math.PI)/n})
    val list = mutableListOf<Segment>()
    for (i in 0 until (points.size-1)){
        list.add(Segment(Pair(func1(points[i]), func2(points[i])), Pair(func1(points[i+1]), func2(points[i+1]))))
    }
    list.add(Segment(Pair(func1(points[points.size-1]), func2(points[points.size-1])), Pair(func1(points[0]), func2(points[0]))))
    return list
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
    return Math.sqrt((pow((boundingBox[0].first-boundingBox[2].first).toInt(),2)+
            pow((boundingBox[0].second-boundingBox[2].second).toInt(),2)).toDouble())
}
fun getBmin(boundingBox: List<Pair<Double, Double>>):List<Double>{
    return listOf<Double>(abs(boundingBox[0].first-boundingBox[1].first), abs(boundingBox[1].first-boundingBox[2].first))
}
fun getBmax(boundingBox: List<Pair<Double, Double>>):List<Double>{
    return listOf<Double>(abs(boundingBox[0].second-boundingBox[1].second), abs(boundingBox[1].second-boundingBox[2].second))
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
//TODO handle vertical and horizontal splits
fun splitBoundingBox(list:List<Segment>,isVertical:Boolean):Pair<List<Segment>,List<Segment>>{
    val tempList = mutableListOf<Pair<Double,Double>>()
    list.forEach {
        tempList.add(it.startPoint)
        tempList.add(it.endPoint)
    }
    val boundingBox = findBoundingBox(tempList)
    val firstList = mutableListOf<Segment>()
    val secondList = mutableListOf<Segment>()
    val halfRec = if (isVertical){
        mutableListOf(Pair((boundingBox[3].first+boundingBox[0].first)/2,boundingBox[0].second),
                Pair((boundingBox[2].first+boundingBox[1].first)/2,boundingBox[1].second),
                boundingBox[2],boundingBox[3])
    } else
        mutableListOf(boundingBox[0],
                Pair(boundingBox[0].first,(boundingBox[0].second+boundingBox[1].second)/2),
                Pair(boundingBox[3].first,(boundingBox[3].second+boundingBox[2].second)/2),
                boundingBox[3])
    list.forEach {
        if (isPointInRectangle((Pair((it.startPoint.first+it.endPoint.first)/2.0,(it.startPoint.second+it.endPoint.second)/2.0)),halfRec) )
            firstList.add(it)
        else
            secondList.add(it)
    }
    return Pair(firstList.toList(),secondList.toList())
}



private fun isPointInRectangle(point:Pair<Double,Double>, rectangle: List<Pair<Double,Double>>):Boolean{
    return ((point.first>=rectangle[2].first && point.first<=rectangle[1].first) &&
            (point.second>=rectangle[1].second && point.second<=rectangle[0].second))
}