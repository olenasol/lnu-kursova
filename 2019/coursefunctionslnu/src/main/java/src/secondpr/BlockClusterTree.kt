package src.secondpr

import Fullmatrix
import Rkmatrix
import Supermatrix
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

        fun buildRkmatrix(t: ClusterTree, s: ClusterTree, n: Int, k: Int): Rkmatrix {

            val rkmatrix = Rkmatrix(k, t.leaf.size, s.leaf.size)
//            //filling Rkmatrix
            for (i in 0 until t.leaf.size) {
                for (v in 0 until rkmatrix.k) {
                    rkmatrix.a[i][v] = 1.0//amatr(t.leaf[i], n, x0, v)
                }
            }
            for (j in 0 until s.leaf.size) {
                for (v in 0 until rkmatrix.k) {
                    if (v == 0) {
                        rkmatrix.b[j][v] = 2.0//bmatr2(s.leaf[j], n, x0, v)

                    } else {
                        rkmatrix.b[j][v] = 5.0//bmatr1(s.leaf[j], n, x0, v)
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
                if (t != null && s != null)
                    spr.rkmatrix = buildRkmatrix(s, t, n, nMin)
                return spr
            } else {
                if (t != null && s!= null  &&!(t.leftTree == null || t.rightTree ==null || s.leftTree == null || s.rightTree == null) && t.leaf.size > 1) {
                    spr.rows = n
                    spr.cols = n
                    spr.blockrows = 2
                    spr.blockcols = 2
                    spr.supermatrix = Array(2) { Array(2) { Supermatrix() } }
                    spr.supermatrix!![0][0] = buildBlockClusterTree(t.leftTree, s.leftTree, spr.supermatrix!![0][0], n, nMin)
                    spr.supermatrix!![0][1] = buildBlockClusterTree(t.rightTree, s.leftTree, spr.supermatrix!![0][1], n, nMin)
                    spr.supermatrix!![1][0] = buildBlockClusterTree(t.leftTree, s.rightTree, spr.supermatrix!![1][0], n, nMin)
                    spr.supermatrix!![1][1] = buildBlockClusterTree(t.rightTree, s.rightTree, spr.supermatrix!![1][1], n, nMin)
                } else {
                    spr.rows = n
                    spr.cols = n
                    spr.supermatrix = null
                    spr.fullmatrix = Fullmatrix()
                    //TODO figure this out
                    if (s!= null)
                        spr.fullmatrix!!.cols = s.leaf.size
                    else print("test ")
                    if (t != null)
                        spr.fullmatrix!!.rows = t.leaf.size
                    else print("test2 ")
                    spr.fullmatrix!!.e = Array(spr.fullmatrix!!.rows) { DoubleArray(spr.fullmatrix!!.cols) }
                    //filling Fullmatrix
                    for (i in 0 until spr.fullmatrix!!.rows) {
                        for (j in 0 until spr.fullmatrix!!.cols)
                            spr.fullmatrix!!.e[i][j] = 8.0//Egtulda(s.leaf[i],t.leaf[j], n)
                    }
                }
                return spr
            }
        }
    }
}