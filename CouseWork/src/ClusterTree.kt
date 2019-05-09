
class ClusterTree{
    var level:Int = 0
    var numberOfLeaf:Int = 0
    lateinit var leaf: IntArray
    var leftTree: ClusterTree? = null
    var rightTree: ClusterTree? = null

    companion object {
        fun buildClusterTree(n:Int):ClusterTree{
            //n=2^p
            val p:Int = Util.log2(n)
            val numbersOfElementsAtLevel = IntArray(p+1){i-> Math.pow(2.0,i.toDouble()).toInt()}
           // numbersOfElementsAtLevel.forEach { println(it) }
            return buildRealClusterTree(0,n-1, 0,0,numbersOfElementsAtLevel)
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
        private fun buildRealClusterTree(begin:Int, end:Int, level:Int, numberOfLeaf: Int, arr:IntArray):ClusterTree{
            val tree = ClusterTree()
            tree.level = level
            tree.numberOfLeaf = numberOfLeaf
            tree.leaf = (begin..end).toList().toIntArray()
            var m = Math.pow(2.0, level.toDouble()).toInt() - arr[level]
            arr[level]--
            if((end - begin+1) != 1) {
                tree.leftTree = buildRealClusterTree(begin, (begin + end + 1) / 2 - 1, level + 1,
                        m, arr)
                m = Math.pow(2.0, level.toDouble()).toInt() - arr[level]
                arr[level]--
                tree.rightTree = buildRealClusterTree((begin + end + 1) / 2, end, level + 1,
                        m, arr)
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