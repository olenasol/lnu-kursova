package src.secondpr

import Egtulda01
import multiplyMatricesV3
import multiplyMatrixByVector
import transposeMatrix
import java.lang.Double.min


class BlockClusterTree {
    companion object {
        //diam<=dist
        fun isAdmissible(s: ClusterTree?, t: ClusterTree?): Boolean {
            if (s != null && t != null) {
                val sList = mutableListOf<Pair<Double,Double>>()
                segments.slice(s.leaf).forEach {
                    sList.add(it.startPoint)
                    sList.add(it.endPoint)
                }
                val tList = mutableListOf<Pair<Double,Double>>()
                segments.slice(t.leaf).forEach {
                    tList.add(it.startPoint)
                    tList.add(it.endPoint)
                }
                val diam1 = calculateDiamOfBoundingBox(findBoundingBox(sList))
                val diam2 = calculateDiamOfBoundingBox(findBoundingBox(tList))
                val diam = min(diam1, diam2)
                val dist = calculateDistanceBetweenBoundingBoxes(findBoundingBox(sList), findBoundingBox(tList))
                return diam <= dist
            } else
                return false
        }

        fun buildRkmatrix(t: ClusterTree, s: ClusterTree, n: Int, m: Int): Rkmatrix {

            val rkmatrix = Rkmatrix(m, t.leaf.size, s.leaf.size, sLeaf = s.leaf, tLeaf = t.leaf)

//            //filling Rkmatrix
            val sList = mutableListOf<Pair<Double,Double>>()
            segments.slice(s.leaf).forEach {
                sList.add(it.startPoint)
                sList.add(it.endPoint)
            }
            val tList = mutableListOf<Pair<Double,Double>>()
            segments.slice(t.leaf).forEach {
                tList.add(it.startPoint)
                tList.add(it.endPoint)
            }
            var v = 0
            if (calculateDiamOfBoundingBox(findBoundingBox(tList))<= calculateDiamOfBoundingBox(findBoundingBox(sList))){
                for (x in 1 until t.leaf.size+1) {
                    val i = t.leaf[x-1]+1
                    v = 0
                    for (v1 in 0 until rkmatrix.m+1) {
                        for (v2 in 0 until rkmatrix.m+1) {
                            if (i== points.size){
                                rkmatrix.a[x-1][v] = norm(Pair(points[0].first - points[i-1].first,
                                        points[0].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),t.split, m)

                            } else
                                rkmatrix.a[x-1][v] = norm(Pair(points[i].first - points[i-1].first,
                                    points[i].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),t.split, m)
                            v++
                        }

                    }
                }
                for (y in 1 until s.leaf.size+1) {
                    val j = s.leaf[y-1]+1
                    v = 0
                    for (v1 in 0 until rkmatrix.m+1) {
                        for (v2 in 0 until rkmatrix.m+1) {
                            val x_v = Pair(t.split[v1].first,t.split[v2].second)
                            if (j == points.size){
                                rkmatrix.b[y-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[0].first - points[j-1].first,
                                        points[0].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)
                            }else
                               rkmatrix.b[y-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[j].first - points[j-1].first,
                                    points[j].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)
                            v++
                        }
                    }
                }
            } else {
                for (x in 1 until s.leaf.size+1) {
                    val i = s.leaf[x-1]+1
                    v=0
                    for (v1 in 0 until rkmatrix.m+1) {
                        for (v2 in 0 until rkmatrix.m+1) {
                            if (i == points.size){
                                rkmatrix.b[x-1][v] = norm(Pair(points[0].first - points[i-1].first,
                                        points[0].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),s.split, m)
                            } else
                                 rkmatrix.b[x-1][v] =norm(Pair(points[i].first - points[i-1].first,
                                    points[i].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),s.split, m)
                            v++
                        }
                    }
                }
                for (y in 1 until t.leaf.size+1) {
                    val j = t.leaf[y-1]+1
                    v = 0
                    for (v1 in 0 until rkmatrix.m+1) {
                        for (v2 in 0 until rkmatrix.m+1) {
                            val x_v = Pair(s.split[v1].first,s.split[v2].second)
                            if (j== points.size ){
                                rkmatrix.a[y-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[0].first - points[j-1].first,
                                       points[0].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)
                            }else
                                rkmatrix.a[y-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[j].first - points[j-1].first,
                                    points[j].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)
                            v++
                        }
                    }
                }
            }
            //end of filling Rkmatrix
            return rkmatrix
        }

        fun buildBlockClusterTree(t: ClusterTree?, s: ClusterTree?, spr: Supermatrix, n: Int, nmin: Int): Supermatrix {
            //admissible -rkmatrix
            if (isAdmissible(t, s)) {
                spr.supermatrix = null
                if (t != null && s != null) {
                    spr.blockrows = 2
                    spr.blockcols =  2
                    spr.rkmatrix = buildRkmatrix(s, t, n, t.split.size -1)
                }
                return spr
            } else {
                if (t != null && s!= null  &&!(t.leftTree == null || t.rightTree ==null || s.leftTree == null || s.rightTree == null) && t.leaf.size > nmin) {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = 2
                    spr.blockcols =  2
                    spr.sLeaf = s.leaf
                    spr.tLeaf = t.leaf
                    spr.supermatrix = Array(2) { Array(2) { Supermatrix(sLeaf = s.leaf, tLeaf = t.leaf) } }
                    spr.supermatrix!![0][0] = buildBlockClusterTree(t.leftTree, s.leftTree, spr.supermatrix!![0][0], n, nmin)
                    spr.supermatrix!![0][1] = buildBlockClusterTree(t.leftTree, s.rightTree, spr.supermatrix!![0][1], n,nmin)
                    spr.supermatrix!![1][0] = buildBlockClusterTree(t.rightTree, s.leftTree, spr.supermatrix!![1][0],n,nmin)
                    spr.supermatrix!![1][1] = buildBlockClusterTree(t.rightTree, s.rightTree, spr.supermatrix!![1][1], n,nmin)
                } else {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = 2
                    spr.blockcols =  2
                    spr.supermatrix = null
                    spr.fullmatrix = Fullmatrix(sLeaf = s!!.leaf, tLeaf = t!!.leaf)
                    spr.fullmatrix!!.cols = s.leaf.size
                    spr.fullmatrix!!.rows = t.leaf.size
                    spr.fullmatrix!!.e = Array(spr.fullmatrix!!.rows) { DoubleArray(spr.fullmatrix!!.cols) }
                    //filling Fullmatrix
                    for (l in t.leaf.indices) {
                        for (m in s.leaf.indices) {
                            val i = t.leaf[l]+1
                            val j = s.leaf[m]+1
                            var integral =0.0
                            integral = if (i==j){
                                if (i == points.size){
                                    Egtulda01() + Math.log(norm(Pair(points[0].first - points[i - 1].first,
                                        points[0].second - points[i - 1].second)))
                                } else {
                                    Egtulda01() + Math.log(norm(Pair(points[i].first - points[i - 1].first,
                                        points[i].second - points[i - 1].second)))
                                }
                            } else{
                                Legendre(6).integrateLogDouble(i,j)
                            }
                            if (i == points.size && j == points.size){
                                spr.fullmatrix!!.e[l][m] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
                                    points[0].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
                                    points[0].second - points[i - 1].second)) * integral
                            } else if (i == points.size){
                                spr.fullmatrix!!.e[l][m] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
                                    points[j].second - points[j - 1].second)) * norm(Pair(points[0].first - points[i - 1].first,
                                    points[0].second - points[i - 1].second)) * integral
                            } else if (j == points.size){
                                spr.fullmatrix!!.e[l][m] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[0].first - points[j - 1].first,
                                    points[0].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
                                    points[i].second - points[i - 1].second)) * integral
                            } else
                                spr.fullmatrix!!.e[l][m] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
                                    points[j].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
                                    points[i].second - points[i - 1].second)) * integral
                        }

                    }
                }
                return spr
            }
        }
        fun MultHMatrixByVector(spr: src.secondpr.Supermatrix, vct: DoubleArray, resVct: DoubleArray): DoubleArray {
            if (spr.supermatrix != null) {
                val a1 = MultHMatrixByVector(spr.supermatrix!![0][0], vct, resVct)
                val a2 = MultHMatrixByVector(spr.supermatrix!![0][1], vct, a1)
                val b1 = MultHMatrixByVector(spr.supermatrix!![1][0], vct, a2)
                val b2 = MultHMatrixByVector(spr.supermatrix!![1][1], vct, b1)
                return b2
            } else {

                if (spr.rkmatrix != null) {
                    val first = multiplyMatrixByVector(transposeMatrix(spr.rkmatrix!!.b), vct.slice(spr.rkmatrix!!.sLeaf).toDoubleArray())
                    val second = multiplyMatrixByVector(spr.rkmatrix!!.a, first)
                    for (i in second.indices){
                        resVct[spr.rkmatrix!!.tLeaf[i]] += second[i]
                    }
                    return resVct
                } else if (spr.fullmatrix != null) {
                    val multVct = vct.slice(spr.fullmatrix!!.sLeaf).toDoubleArray()
                    val res= multiplyMatrixByVector(spr.fullmatrix!!.e, multVct)
                    for (i in res.indices){
                        resVct[spr.fullmatrix!!.tLeaf[i]] += res[i]
                    }
                    return resVct
                }
            }
            return DoubleArray(1)
        }



        fun transposeMatrix(matrix:Array<DoubleArray>): Array<DoubleArray>{
            val transpose = Array(matrix[0].size){DoubleArray(matrix.size)}
            for (i in 0..matrix.size - 1) {
                for (j in 0..matrix[0].size - 1) {
                    transpose[j][i] = matrix[i][j]
                }
            }
            return transpose
        }

        fun getNormalMatrix(spr:Supermatrix):Array<DoubleArray>{
            if(spr.supermatrix != null){
                val a1 = getNormalMatrix(spr.supermatrix!![0][0])
                val a2 = getNormalMatrix(spr.supermatrix!![0][1])
                val b1 = getNormalMatrix(spr.supermatrix!![1][0])
                val b2 = getNormalMatrix(spr.supermatrix!![1][1])
                val array = Array(a1.size+ b1.size){DoubleArray(a1[0].size+a2[0].size)}
                for (i in 0 until a1.size)
                    for (j in 0 until a1[0].size) {
                        array[i][j] = a1[i][j]
                    }
                for (i in 0 until b1.size)
                    for (j in 0 until b1[0].size) {
                        array[a1.size + i][ j] = b1[i][j ]
                    }
                for (i in 0 until b2.size)
                    for (j in 0 until b2[0].size) {
                        array[a2.size + i][b1[0].size +j] = b2[i][j]
                    }
                for (i in 0 until a2.size)
                    for (j in 0 until a2[0].size) {
                        array[i][a1[0].size + j] = a2[i ][j]
                    }
                return array
            }
            else {
                if (spr.rkmatrix != null) {
                    return transposeMatrix(multiplyMatricesV3(spr.rkmatrix!!.a,transposeMatrix(spr.rkmatrix!!.b)))
                } else if (spr.fullmatrix != null) {
                    return spr.fullmatrix!!.e
                }
            }
            return Array(1){ DoubleArray(1) }
        }
    }



}