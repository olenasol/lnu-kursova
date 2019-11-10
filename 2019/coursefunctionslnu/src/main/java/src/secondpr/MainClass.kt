package src.secondpr

import Supermatrix
import src.secondpr.ClusterTree.Companion.buildClusterTree


fun main(){
    val first = listOf(Pair(7.0,8.0), Pair(7.0,2.0), Pair(2.0,2.0), Pair(2.0,8.0))
    val second = listOf(Pair(14.0,6.0), Pair(16.0,2.0), Pair(10.0,1.0), Pair(8.0,5.0))
    val n = 64
    val k = 8
    val clusterTree = buildClusterTree(n,k)
    val sprm = BlockClusterTree.buildBlockClusterTree(clusterTree,clusterTree,Supermatrix(),n,k)
    println(calculateDistanceBetweenBoundingBoxes(first,second))

//    print(calculateDistanceBetweenTwoSides(Segment(Pair(1.0,3.0), Pair(5.0, 3.0)),
//            Segment(Pair(6.0,6.0), Pair(10.0,6.0))))
//    val clusterTree = ClusterTree.buildClusterTree(8,1)
//    println(getTreeByIndex(clusterTree,0,0))
}