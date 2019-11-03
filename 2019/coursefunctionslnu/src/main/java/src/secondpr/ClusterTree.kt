package src.secondpr

class ClusterTree{
    var level:Int = 0
    var numberOfLeaf:Int = 0
    lateinit var leaf: List<Segment>
    var leftTree: ClusterTree? = null
    var rightTree: ClusterTree? = null

    companion object {
        fun buildClusterTree(n:Int,k:Int):ClusterTree{
            val nmin = 2
            //n=2^p
            val p:Int = Util.log2(n)
            val numbersOfElementsAtLevel = IntArray(p+2){i-> Math.pow(2.0,i.toDouble()).toInt()}
            val boundaryElements = buildBoundaryElements(n=8,func1 = {t:Double-> Math.cos(t) }, func2 = { t:Double-> Math.sin(t) })
            return buildRealClusterTree(boundaryElements,0, 0,numbersOfElementsAtLevel,nmin)
        }

        fun getNumberOfNodes(p: Int): Int {
            return if (p == 1) {
                3
            } else {
                2 * getNumberOfNodes(p - 1) + 1
            }
        }
        // arr is used to calculate bottom indexes(numberOfLeaf) of tree child,
        // original - for n=8 [1,2,4,8] - number of children on each level
        private fun buildRealClusterTree(list: List<Segment>, level:Int, numberOfLeaf: Int, arr:IntArray,nmin:Int):ClusterTree{
            val tree = ClusterTree()
            tree.level = level
            tree.numberOfLeaf = numberOfLeaf
            tree.leaf = list
            var m = Math.pow(2.0, level.toDouble()).toInt() - arr[level]
            arr[level]--
            if(list.size > nmin) {
                val split = splitBoundingBox(list,true)
                tree.leftTree = buildRealClusterTree(split.first, level + 1,
                        m, arr,nmin)
                m = Math.pow(2.0, level.toDouble()).toInt() - arr[level]
                arr[level]--
                tree.rightTree = buildRealClusterTree(split.second, level + 1,
                        m, arr,nmin)
            } else{
                tree.leftTree = null
                tree.rightTree = null
            }
            return tree
        }

        fun getTreeByIndex(parentTree : ClusterTree?,level:Int, num:Int):ClusterTree?{
            if (parentTree == null){
                return null
            } else{
                if(parentTree.level == level && parentTree.numberOfLeaf == num){
                    return parentTree
                } else {
                    getTreeByIndex(parentTree.rightTree, level, num)?.let { return it }
                    getTreeByIndex(parentTree.leftTree, level, num)?.let { return it }
                    return null
                }
            }
        }
    }
}