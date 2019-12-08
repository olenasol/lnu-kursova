package src.secondpr

class ClusterTree{
    var level:Int = 0
    lateinit var leaf: List<Int>
    lateinit var split: List<Pair<Double,Double>>
    var leftTree: ClusterTree? = null
    var rightTree: ClusterTree? = null

    companion object {
        fun buildClusterTree(n:Int,m:Int, nmin: Int):ClusterTree{
            val boundaryElements = buildBoundaryElements(n=n,func1 = {t:Double-> Math.cos(t) }, func2 = { t:Double-> Math.sin(t) })
            return buildRealClusterTree(boundaryElements,0,nmin, m)
        }

        private fun buildRealClusterTree(list: List<Int>, level:Int, nmin:Int, m: Int):ClusterTree{
            val tree = ClusterTree()
            tree.level = level
            tree.leaf = list
            tree.split = getSplitOnSegmentab(list, m)
            if(list.size > nmin) {
                val split = splitBoundingBox(list)
                tree.leftTree = buildRealClusterTree(split.first, level + 1,
                        nmin,m)
                tree.rightTree = buildRealClusterTree(split.second, level + 1,
                       nmin,m)
            } else{
                tree.leftTree = null
                tree.rightTree = null
            }
            return tree
        }

    }
}