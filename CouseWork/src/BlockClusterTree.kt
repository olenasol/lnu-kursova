




class BlockClusterTree {
    companion object {
        //diam<=dist
        fun isAdmissible(s: ClusterTree?, t: ClusterTree?): Boolean {
            if(s!= null && t!= null) {
                val diam = 1 / Math.pow(2.0, s.level.toDouble())
                val dist = (Math.abs(s.numberOfLeaf - t.numberOfLeaf) - 1) * diam
                print("{")
                t.leaf.forEach { print(it.toString()+" ") }
                print("}x{")
                s.leaf.forEach { print(it.toString()+" ") }
                println("} - "+(diam <= dist).toString())
                return diam <= dist
            }else
                return false

        }

        fun buildRkmatrix(t: ClusterTree, s: ClusterTree, n: Int): Rkmatrix {
            val k=100
            val rkmatrix = Rkmatrix(k, t.leaf.size, s.leaf.size)
//            //filling Rkmatrix
//            val x0 = (t.leaf[0].toDouble() + 0.5 * t.leaf.size.toDouble()) * (1.0 / n.toDouble())
//            var m = 0
//            for (i in 0 until t.leaf.size) {
//                for (v in 1 until rkmatrix.k) {
//                    rkmatrix.a[m] = amatr(t.leaf[i], n, x0, v)
//                    m++
//                }
//            }
//            m = 0
//            for (i in 0 until s.leaf.size) {
//                for (v in 1 until rkmatrix.k) {
//                    if (v == 0) {
//                        rkmatrix.b[m] = bmatr2(s.leaf[i], n, x0, v)
//
//                        m++
//                    } else {
//                        rkmatrix.b[m] = bmatr1(s.leaf[i], n, x0, v)
//
//                        m++
//                    }
//                }
//            }
            //TODO remove
            for(i in 0 until rkmatrix.a.size){
                for (j in 0 until rkmatrix.a[i].size){
                    rkmatrix.a[i][j] = 2.0
                }
            }
            for(i in 0 until rkmatrix.b.size){
                for (j in 0 until rkmatrix.b[i].size){
                    rkmatrix.b[i][j] = 5.0
                }
            }
            //end of filling Rkmatrix
            return rkmatrix
        }

        fun buildBlockClusterTree(t: ClusterTree?, s: ClusterTree?, spr: Supermatrix, n: Int): Supermatrix {
            //admissible -rkmatrix
            if (isAdmissible(t, s)) {
                spr.supermatrix = null
                if(t!= null && s!= null)
                    spr.rkmatrix = buildRkmatrix(t, s, n)
                return spr
            } else {
                if (t?.leaf?.size != 1)
                {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = 2
                    spr.blockcols = 2
                    spr.supermatrix = Array(2){ Array(2){Supermatrix()} }
                    spr.supermatrix!![0][0] = buildBlockClusterTree(t?.leftTree, s?.leftTree, spr.supermatrix!![0][ 0],n)
                    spr.supermatrix!![0][1] = buildBlockClusterTree(t?.rightTree, s?.leftTree, spr.supermatrix!![0][1],n)
                    spr.supermatrix!![1][0] = buildBlockClusterTree(t?.leftTree, s?.rightTree, spr.supermatrix!![1][0],n)
                    spr.supermatrix!![1][1] = buildBlockClusterTree(t?.rightTree, s?.rightTree, spr.supermatrix!![1][1],n)
                }
                else
                {
                    spr.rows = n;
                    spr.cols = n;
                    spr.supermatrix = null;
                    spr.fullmatrix = Fullmatrix()
                    spr.fullmatrix!!.cols = 0;
                    spr.fullmatrix!!.rows = 0;
                    spr.fullmatrix!!.e = DoubleArray(1)
                    //filling Fullmatrix
                    if (s != null) {
                        //TODO change back
                        spr.fullmatrix!!.e[0] = 3.0//Egtulda(t.leaf[0],s.leaf[0], n)
                    }
                }
                return spr
            }
        }

        fun MultHMatrixByVector(spr: Supermatrix, vct: DoubleArray): DoubleArray {
            if (spr.supermatrix != null) {
                val n = vct.size
                val vct1: DoubleArray = DoubleArray(Integer.valueOf(n / 2))
                val vct2: DoubleArray = DoubleArray(Integer.valueOf(n / 2))
                for (i in 0..(n / 2 - 1)) {
                    vct1[i] = vct[i]
                    vct2[i] = vct[i + Integer.valueOf(n / 2)]
                }
                val a1 = MultHMatrixByVector(spr.supermatrix!![0][0], vct1)
                val a2 = MultHMatrixByVector(spr.supermatrix!![0][1], vct2)
                val b1 = MultHMatrixByVector(spr.supermatrix!![1][0], vct1)
                val b2 = MultHMatrixByVector(spr.supermatrix!![1][1], vct2)
                val res: DoubleArray = DoubleArray(Integer.valueOf(n))
                for (i in 0..(n / 2 - 1)) {
                    res[i] = a1[i] + a2[i]
                    res[i + Integer.valueOf(n / 2)] = b1[i] + b2[i]
                }
                return res;
            } else {
                if (spr.rkmatrix != null) {
                    val first = multiplyMatrixByVector(transposeMatrix(spr.rkmatrix!!.b), vct)
                    val second = multiplyMatrixByVector(spr.rkmatrix!!.a, first)
                    return second
                } else if (spr.fullmatrix != null) {
                    var res = DoubleArray(1)
                    res[0] = multiplyVectorByVector(spr.fullmatrix!!.e, vct)
                    return res;
                }
            }
            return DoubleArray(1)
        }
        public fun getNormalMatrix(spr:Supermatrix):Array<DoubleArray>{
            if(spr.supermatrix != null){
                val a1 = getNormalMatrix(spr.supermatrix!![0][0])
                val a2 = getNormalMatrix(spr.supermatrix!![0][1])
                val b1 = getNormalMatrix(spr.supermatrix!![1][0])
                val b2 = getNormalMatrix(spr.supermatrix!![1][1])
                val array = Array(a1.size+b1.size){DoubleArray(a1[0].size+a2[0].size)}
                for (i in 0 until a1.size)
                    for (j in 0 until a1[0].size)
                        array[i][j] = a1[i][j]
                for (i in 0 until a1.size)
                    for (j in a1.size until (a1[0].size+a2[0].size))
                        array[i][j] = a2[i][j-a1.size]
                for (i in a1.size until (a1.size+b1.size))
                    for (j in a1.size until (a1[0].size+a2[0].size))
                        array[i][j] = b2[i-a1.size][j-a1.size]
                for (i in a1.size until (b1.size+a1.size))
                    for (j in 0 until a1[0].size)
                        array[i][j] = b1[i-a1.size][j]
                return array
            }
            else {
                if (spr.rkmatrix != null) {
                    val temp = multiplyMatricesV3(spr.rkmatrix!!.a,transposeMatrix(spr.rkmatrix!!.b))
                    return multiplyMatricesV3(spr.rkmatrix!!.a,transposeMatrix(spr.rkmatrix!!.b))
                } else if (spr.fullmatrix != null) {
                    return arrayOf(doubleArrayOf(spr.fullmatrix!!.e[0]))
                }
            }
            return Array(1){ DoubleArray(1) }
        }
    }

}