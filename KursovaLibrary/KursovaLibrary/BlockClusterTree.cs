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

        public static Supermatrix BuildBlockClusterTree(ClusterTree t,ClusterTree s,Supermatrix spr,int n)
        {
            if (IsAdmissible(t, s))
            {
                spr.s = null;
                spr.r = new Rkmatrix();
                spr.r.rows = t.leaf.Length;
                spr.r.cols = s.leaf.Length;
                spr.r.k = spr.r.rows;
                spr.r.a = new double[spr.r.rows * spr.r.cols];
                spr.r.b = new double[spr.r.rows * spr.r.cols];
                for (int i = 0; i < spr.r.a.Length; i++)
                {
                    spr.r.a[i] = 8;
                    spr.r.b[i] = 1;
                }
                    //int p = (int)Math.Pow(2, s.level);
                    //double[] podil = new double[p + 1];
                    //for (int i = 0; i < podil.Length; i++)
                    //{
                    //    podil[i] = i / (double)p;
                    //}
                    //double a = podil[s.numberOfLeaf];
                    //double b = podil[s.numberOfLeaf + 1];
                    //double x0=(a+b)/2;
                    //int m=0;
                    //for(int i=0;i<t.leaf.Length;i++)
                    //{
                    //    for (int v=0;v<spr.r.k;v++){
                    //        spr.r.a[m] = FunctionsBEM.amatr(t.leaf[i],n,x0,v);
                    //        m++;
                    //    }
                    //}
                    //m = 0;
                    //for (int i = 0; i < s.leaf.Length; i++)
                    //{
                    //    for (int v = 0; v < spr.r.k; v++)
                    //    {
                    //        if (v == 0)
                    //        {
                    //            spr.r.b[m] = FunctionsBEM.bmatr2(s.leaf[i], n, x0, v);
                    //            m++;
                    //        }
                    //        else
                    //        {
                    //            spr.r.b[m] = FunctionsBEM.bmatr1(s.leaf[i], n, x0, v);
                    //            m++;
                    //        }
                    //    }
                    //}

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
                    spr.s = new Supermatrix[spr.blockrows, spr.blockcols];
                    for (int i = 0; i < spr.s.GetLength(0); i++)
                    {
                        for (int j = 0; j < spr.s.GetLength(1); j++)
                        {
                            spr.s[i, j] = new Supermatrix();
                        }
                    }
                    spr.s[0, 0] = BuildBlockClusterTree(t.leftTree, s.leftTree, spr.s[0, 0],n);
                    spr.s[0, 1] = BuildBlockClusterTree(t.rightTree, s.leftTree, spr.s[0, 1],n);
                    spr.s[1, 0] = BuildBlockClusterTree(t.leftTree, s.rightTree, spr.s[1, 0],n);
                    spr.s[1, 1] = BuildBlockClusterTree(t.rightTree, s.rightTree, spr.s[1, 1],n);
                }
                else
                {
                    spr.rows = n;
                    spr.cols = n;
                    spr.s = null;
                    spr.f = new Fullmatrix();
                    spr.f.cols = 0;
                    spr.f.rows = 0;
                    spr.f.e = new double[1];
                    spr.f.e[0] = 3;
                   // spr.f.e[0] = FunctionsBEM.Egtulda(t.leaf[0], s.leaf[0], n);
                }
                return spr;
            }
            
        }

        static public double[] MultHMatrixByVector(Supermatrix spr, double[] vct)
        {
            if (spr.s != null)
            {
                int n=vct.Length;
                double[] vct1=new double[(int)(n/2)];
                double[] vct2=new double[(int)(n/2)];
                for (int i=0;i<n/2;i++){
                    vct1[i]=vct[i];
                    vct2[i]=vct[i+(int)(n/2)];
                }
                double[] a1=MultHMatrixByVector(spr.s[0, 0], vct1);
                double[] a2=MultHMatrixByVector(spr.s[0, 1], vct2);
                double[] b1=MultHMatrixByVector(spr.s[1, 0], vct1);
                double[] b2=MultHMatrixByVector(spr.s[1, 1], vct2);
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
                if (spr.r != null)
                {
                    double[,] tempa=new double[spr.r.rows,spr.r.cols];
                    double[,] tempb=new double[spr.r.rows,spr.r.cols];
                    int k = 0;
                    for (int i = 0; i < spr.r.rows; i++)
                    {
                        for (int j = 0; j < spr.r.cols; j++)
                        {
                            tempa[i, j] = spr.r.a[k];
                            tempb[i, j] = spr.r.b[k];
                            k++;
                        }
                    }
                    double[] first = GradientMethod.MultiplyMatrixByVector(tempb, vct);
                    double[] second = GradientMethod.MultiplyMatrixByVector(tempa, first);
                    return second;
                }
                else if (spr.f != null)
                {
                    double[] res = new double[1];
                    res[0]=GradientMethod.MultiplyVectorByVector(spr.f.e, vct);
                    return res;
                }
            }
            double[] r = new double[1];
            r[0] = 0;
            return r;
        }
    }
}
