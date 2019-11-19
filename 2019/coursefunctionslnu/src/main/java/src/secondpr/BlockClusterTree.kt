package src.secondpr

import multiplyMatrixByVector
import transposeMatrix
import java.lang.Double.min


class BlockClusterTree {
    companion object {
        //diam<=dist
        fun isAdmissible(s: ClusterTree?, t: ClusterTree?): Boolean {
            if (s != null && t != null) {
                val sList = mutableListOf<Pair<Double,Double>>()
                s.leaf.forEach {
                    sList.add(it.startPoint)
                    sList.add(it.endPoint)
                }
                val tList = mutableListOf<Pair<Double,Double>>()
                t.leaf.forEach {
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

            val rkmatrix = Rkmatrix(m, t.leaf.size, s.leaf.size)
//            //filling Rkmatrix
            val sList = mutableListOf<Pair<Double,Double>>()
            s.leaf.forEach {
                sList.add(it.startPoint)
                sList.add(it.endPoint)
            }
            val tList = mutableListOf<Pair<Double,Double>>()
            t.leaf.forEach {
                tList.add(it.startPoint)
                tList.add(it.endPoint)
            }
            var v = 0
            if (calculateDiamOfBoundingBox(findBoundingBox(tList))<= calculateDiamOfBoundingBox(findBoundingBox(sList))){
                for (i in 1 until t.leaf.size+1) {
                    v = 0
                    for (v1 in 0 until rkmatrix.m) {
                        for (v2 in 0 until rkmatrix.m) {
                            rkmatrix.a[i-1][v] = norm(Pair(points[i].first - points[i-1].first,
                                    points[i].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),t.split, m)
                            v++
                        }
                    }
                }
                for (j in 1 until s.leaf.size+1) {
                    v = 0
                    for (v1 in 0 until rkmatrix.m) {
                        for (v2 in 0 until rkmatrix.m) {
                            val x_v = Pair(t.split[v1].first,t.split[v2].second)
                            rkmatrix.b[j-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[j].first - points[j-1].first,
                                    points[j].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)
                            v++
                        }
                    }
                }
            } else {
                for (i in 1 until s.leaf.size+1) {
                    v=0
                    for (v1 in 0 until rkmatrix.m) {
                        for (v2 in 0 until rkmatrix.m) {
                            rkmatrix.b[i-1][v] = norm(Pair(points[i].first - points[i-1].first,
                                    points[i].second - points[i-1].second)) * Legendre(6).integrateLagrange(i, Pair(v1,v2),s.split, m)
                            v++
                        }
                    }
                }
                for (j in 1 until t.leaf.size+1) {
                    v = 0
                    for (v1 in 0 until rkmatrix.m) {
                        for (v2 in 0 until rkmatrix.m) {
                            val x_v = Pair(s.split[v1].first,s.split[v2].second)
                            rkmatrix.a[j-1][v] = -(1.0/(2.0*Math.PI))*norm(Pair(points[j].first - points[j-1].first,
                                    points[j].second - points[j-1].second)) * Legendre(6).integrateLog(x_v,j)

                        }
                    }
                }
            }
            //end of filling Rkmatrix
            return rkmatrix
        }

        fun buildBlockClusterTree(t: ClusterTree?, s: ClusterTree?, spr: Supermatrix, n: Int, k: Int): Supermatrix {
            //admissible -rkmatrix
            val nMin = 2 * k
            if (isAdmissible(t, s)) {
                spr.supermatrix = null
                if (t != null && s != null) {
                    spr.blockrows = t.leaf.size
                    spr.blockcols = s.leaf.size
                    spr.rkmatrix = buildRkmatrix(s, t, n, s.split.size - 1)
                }
                return spr
            } else {
                if (t != null && s!= null  &&!(t.leftTree == null || t.rightTree ==null || s.leftTree == null || s.rightTree == null) && t.leaf.size > 1) {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = t.leaf.size
                    spr.blockcols =  s.leaf.size
                    spr.supermatrix = Array(2) { Array(2) { Supermatrix() } }
                    spr.supermatrix!![0][0] = buildBlockClusterTree(t.leftTree, s.leftTree, spr.supermatrix!![0][0], n, nMin)
                    spr.supermatrix!![0][1] = buildBlockClusterTree(t.rightTree, s.leftTree, spr.supermatrix!![0][1], n, nMin)
                    spr.supermatrix!![1][0] = buildBlockClusterTree(t.leftTree, s.rightTree, spr.supermatrix!![1][0], n, nMin)
                    spr.supermatrix!![1][1] = buildBlockClusterTree(t.rightTree, s.rightTree, spr.supermatrix!![1][1], n, nMin)
                } else {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = t!!.leaf.size
                    spr.blockcols =  s!!.leaf.size
                    spr.supermatrix = null
                    spr.fullmatrix = Fullmatrix()
                    spr.fullmatrix!!.cols = s.leaf.size
                    spr.fullmatrix!!.rows = t.leaf.size
                    spr.fullmatrix!!.e = Array(spr.fullmatrix!!.rows) { DoubleArray(spr.fullmatrix!!.cols) }
                    //filling Fullmatrix
                    for (i in 1 until spr.fullmatrix!!.rows+1) {
                        for (j in 1 until spr.fullmatrix!!.cols+1) {
                            spr.fullmatrix!!.e[i-1][j-1] = -(1.0 / (2.0 * Math.PI)) * norm(Pair(points[j].first - points[j - 1].first,
                                    points[j].second - points[j - 1].second)) * norm(Pair(points[i].first - points[i - 1].first,
                                    points[i].second - points[i - 1].second)) * Legendre(6).integrateLogDouble(i, j)
                        }

                    }
                }
                return spr
            }
        }
        fun MultHMatrixByVector(spr: src.secondpr.Supermatrix, vct: DoubleArray): DoubleArray {
            if (spr.supermatrix != null) {
                val n = vct.size
                val breakPoint = spr.supermatrix!![0][0].blockcols
                val breakPoint2 = spr.supermatrix!![1][0].blockcols
                val a1 = MultHMatrixByVector(spr.supermatrix!![0][0], vct.copyOfRange(0, breakPoint))
                val a2 = MultHMatrixByVector(spr.supermatrix!![0][1], vct.copyOfRange(breakPoint, vct.size))
                val b1 = MultHMatrixByVector(spr.supermatrix!![1][0], vct.copyOfRange(0, breakPoint2))
                val b2 = MultHMatrixByVector(spr.supermatrix!![1][1], vct.copyOfRange(breakPoint2, vct.size))
                val res: DoubleArray = DoubleArray(Integer.valueOf(n))
                println("-----------------------")
                println("\t a1.size = "+a1.size +"\t a2.size = "+a2.size +"\t b1.size = "+b1.size +"\t b2.size = "+b2.size )
                for (i in 0..(n / 2 - 1)) {
                    res[i] = a1[i] + a2[i]
                    res[i + Integer.valueOf(n / 2)] = b1[i] + b2[i]
                }
                return res
            } else {
                if (spr.rkmatrix != null) {
                    val first = multiplyMatrixByVector(transposeMatrix(spr.rkmatrix!!.b), vct)
                    val second = multiplyMatrixByVector(spr.rkmatrix!!.a, first)
                    return second
                } else if (spr.fullmatrix != null) {
                    val res= multiplyMatrixByVector(spr.fullmatrix!!.e, vct)
                    return res
                }
            }
            return DoubleArray(1)
        }
    }

}