package src.secondpr

import java.awt.Rectangle

fun buildBoundaryElements(n:Int = 10,func1:(x:Double)->Double,func2:(x:Double)->Double):List<Pair<Double,Double>>{
    val points = DoubleArray(2*n, { j -> (j*Math.PI)/n})
    val list = mutableListOf<Pair<Double,Double>>()
    points.forEach { list.add(Pair(func1(it),func2(it))) }
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

fun splitBoundingBox(list:List<Pair<Double,Double>>,isVertical:Boolean):Pair<List<Pair<Double,Double>>,List<Pair<Double,Double>>>{
    val boundingBox = findBoundingBox(list)
    val firstList = mutableListOf<Pair<Double,Double>>()
    val secondList = mutableListOf<Pair<Double,Double>>()
    val halfRec = if (isVertical){
        mutableListOf(Pair((boundingBox[3].first+boundingBox[0].first)/2,boundingBox[0].second),
                Pair((boundingBox[2].first+boundingBox[1].first)/2,boundingBox[0].second),
                boundingBox[2],boundingBox[3])
    } else
        mutableListOf(boundingBox[0],
                Pair(boundingBox[0].first,(boundingBox[0].second+boundingBox[1].second)/2),
                Pair(boundingBox[3].first,(boundingBox[3].second+boundingBox[2].second)/2),
                boundingBox[3])
    list.forEach {
        if (isPointInRectangle(it,halfRec))
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