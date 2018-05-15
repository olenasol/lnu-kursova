using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KursovaLibrary
{
    public class BlockClusterTree
    {
        public static bool IsAdmissible(ClusterTree s, ClusterTree t)
        {
            int p = (int)Math.Pow(2, s.level);
            double[] podil = new double[p + 1];
            for (int i = 0; i < podil.Length; i++)
            {
                podil[i] = i / (double)p;
            }
            double a = podil[s.numberOfLeaf];
            double b = podil[s.numberOfLeaf + 1];
            double c = podil[t.numberOfLeaf];
            double d = podil[t.numberOfLeaf + 1];
            double diam = b - a;
            double dist = c - b;
            if (a > c)
            {
                dist = a - d;
            }
            else
            {
                dist = c - b;
            }
            if (dist < 0)
            {
                dist = 0;
            }
            if (diam <= dist)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        private static Rkmatrix buildRkmatrix(ClusterTree t, ClusterTree s, int n)
        {
            Rkmatrix rkmatrix = new Rkmatrix();
            rkmatrix.rows = t.leaf.Length;
            rkmatrix.cols = s.leaf.Length;
            rkmatrix.k = rkmatrix.rows;
            rkmatrix.a = new double[rkmatrix.rows * rkmatrix.cols];
            rkmatrix.b = new double[rkmatrix.rows * rkmatrix.cols];
            //filling Rkmatrix
            double x0 = ((double)t.leaf[0] + 0.5 * (double)t.leaf.Length) * (1.0 / (double)n);
            int m = 0;
            for (int i = 0; i < t.leaf.Length; i++)
            {
                for (int v = 1; v < rkmatrix.k; v++)
                {
                    rkmatrix.a[m] = FunctionsBEM.amatr(t.leaf[i], n, x0, v);
                    m++;
                }
            }
            m = 0;
            for (int i = 0; i < s.leaf.Length; i++)
            {
                for (int v = 1; v < rkmatrix.k; v++)
                {
                    if (v == 0)
                    {
                        rkmatrix.b[m] = FunctionsBEM.bmatr2(s.leaf[i], n, x0, v);
                      
                        m++;
                    }
                    else
                    {
                        rkmatrix.b[m] = FunctionsBEM.bmatr1(s.leaf[i], n, x0, v);
                       
                        m++;
                    }
                }
            }

            for (int i = 0; i < rkmatrix.a.Length; i++)
            {
                rkmatrix.a[i] = -0.25;
                rkmatrix.b[i] = 1;
            }
            //end of filling Rkmatrix
            return rkmatrix;
        }

        public static Supermatrix BuildBlockClusterTree(ClusterTree t,ClusterTree s,Supermatrix spr,int n)
        {
            if (IsAdmissible(t, s))
            {
                spr.supermatrix = null;
                spr.rkmatrix = buildRkmatrix(t, s, n);
                return spr;
            }
            else
            {
                if (t.leaf.Length != 1)
                {
                    spr.rows = n;
                    spr.cols = n;
                    spr.blockrows = 2;
                    spr.blockcols = 2;
                    spr.supermatrix = new Supermatrix[spr.blockrows, spr.blockcols];
                    for (int i = 0; i < spr.supermatrix.GetLength(0); i++)
                    {
                        for (int j = 0; j < spr.supermatrix.GetLength(1); j++)
                        {
                            spr.supermatrix[i, j] = new Supermatrix();
                        }
                    }
                    spr.supermatrix[0, 0] = BuildBlockClusterTree(t.leftTree, s.leftTree, spr.supermatrix[0, 0],n);
                    spr.supermatrix[0, 1] = BuildBlockClusterTree(t.rightTree, s.leftTree, spr.supermatrix[0, 1],n);
                    spr.supermatrix[1, 0] = BuildBlockClusterTree(t.leftTree, s.rightTree, spr.supermatrix[1, 0],n);
                    spr.supermatrix[1, 1] = BuildBlockClusterTree(t.rightTree, s.rightTree, spr.supermatrix[1, 1],n);
                }
                else
                {
                    spr.rows = n;
                    spr.cols = n;
                    spr.supermatrix = null;
                    spr.fullmatrix = new Fullmatrix();
                    spr.fullmatrix.cols = 0;
                    spr.fullmatrix.rows = 0;
                    spr.fullmatrix.e = new double[1];
                    //filling Fullmatrix
                    spr.fullmatrix.e[0] = FunctionsBEM.Egtulda(t.leaf[0],s.leaf[0], n);
                    //spr.fullmatrix.e[0] = 1;
                }
                return spr;
            }
            
        }

        static public double[] MultHMatrixByVector(Supermatrix spr, double[] vct)
        {
            if (spr.supermatrix != null)
            {
                int n=vct.Length;
                double[] vct1=new double[(int)(n/2)];
                double[] vct2=new double[(int)(n/2)];
                for (int i=0;i<n/2;i++){
                    vct1[i]=vct[i];
                    vct2[i]=vct[i+(int)(n/2)];
                }
                double[] a1=MultHMatrixByVector(spr.supermatrix[0, 0], vct1);
                double[] a2=MultHMatrixByVector(spr.supermatrix[0, 1], vct2);
                double[] b1=MultHMatrixByVector(spr.supermatrix[1, 0], vct1);
                double[] b2=MultHMatrixByVector(spr.supermatrix[1, 1], vct2);
                double[] res = new double[n];
                for (int i = 0; i < (int)n / 2; i++)
                {
                    res[i] = a1[i] + a2[i];
                    res[i + (int)n / 2] = b1[i] + b2[i];
                }
                return res;
            }
            else
            {
                if (spr.rkmatrix != null)
                {
                    double[,] tempa=new double[spr.rkmatrix.rows,spr.rkmatrix.cols];
                    double[,] tempb=new double[spr.rkmatrix.rows,spr.rkmatrix.cols];
                    int k = 0;
                    for (int i = 0; i < spr.rkmatrix.rows; i++)
                    {
                        for (int j = 0; j < spr.rkmatrix.cols; j++)
                        {
                            tempa[i, j] = spr.rkmatrix.a[k];
                            tempb[i, j] = spr.rkmatrix.b[k];
                            k++;
                        }
                    }
                    double[] first = GradientMethod.MultiplyMatrixByVector(tempb, vct);
                    double[] second = GradientMethod.MultiplyMatrixByVector(tempa, first);
                    return second;
                }
                else if (spr.fullmatrix != null)
                {
                    double[] res = new double[1];
                    res[0]=GradientMethod.MultiplyVectorByVector(spr.fullmatrix.e, vct);
                    return res;
                }
            }
            double[] r = new double[1];
            r[0] = 0;
            return r;
        }
    }
}
