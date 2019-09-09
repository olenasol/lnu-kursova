package src.secondpr

import src.secondpr.ClusterTree.Companion.getTreeByIndex
import java.lang.Math.cos
import java.lang.Math.sin


fun main(){
    val clusterTree = ClusterTree.buildClusterTree(8,1)
    println(getTreeByIndex(clusterTree,0,0))
}