package src.secondpr

import Supermatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree
import src.secondpr.ClusterTree.Companion.getTreeByIndex
import java.lang.Math.cos
import java.lang.Math.sin


fun main(){
    val first = listOf<Pair<Double, Double>>(Pair(7.0,8.0), Pair(7.0,2.0), Pair(2.0,2.0), Pair(2.0,8.0))
    val second = listOf<Pair<Double, Double>>(Pair(14.0,6.0), Pair(16.0,2.0), Pair(10.0,1.0), Pair(8.0,5.0))
    val sprm = BlockClusterTree.buildBlockClusterTree(buildClusterTree(8,1),buildClusterTree(8,1),Supermatrix(),8,1)
    print(calculateDistanceBetweenBoundingBoxes(first,second))
//    print(calculateDistanceBetweenTwoSides(Segment(Pair(1.0,3.0), Pair(5.0, 3.0)),
//            Segment(Pair(6.0,6.0), Pair(10.0,6.0))))
//    val clusterTree = ClusterTree.buildClusterTree(8,1)
//    println(getTreeByIndex(clusterTree,0,0))
}