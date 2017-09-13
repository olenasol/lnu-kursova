using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace KursovaLibrary
{
    public class GradientMethod
    {
        public static double MultiplyVectorByVector(double[] a, double[] b)
        {
            double res = 0;
            for (int i = 0; i < a.Length; i++)
            {
                res = res + a[i] * b[i];
            }
            return res;
        }
        public static double[] MultiplyMatrixByVector(double[,] a, double[] b)
        {
            int n = b.Length;
            double[] x = new double[n];
            for (int j = 0; j < n; j++)
            {
                for (int i = 0; i < n; i++)
                {
                    x[j] = x[j] + a[j, i] * b[i];
                }
            }
            return x;
        }

        public static double[] ConjugateGradientMethod(double[,] a,double[] b,double[] x0)
        {
            int n=b.Length;
            double[] x1 = new double[n];
            double[] temp = MultiplyMatrixByVector(a, x0);
            double[] r0 = new double[n];
            double[] p0 = new double[n];
            double[] r1 = new double[n];
            double[] p1 = new double[n];
            double alphak = 0;
            double betak = 0;
            for (int i = 0; i < n; i++)
            {
                r0[i] = b[i] - temp[i];
            }
            p0 = r0;
            int k = 0;
            while (k!=n)
            {
                double temp1 = MultiplyVectorByVector(r0, r0);
                double[] temp2 = MultiplyMatrixByVector(a, p0);
                double temp3 = MultiplyVectorByVector(temp2, p0);
                alphak = temp1 / temp3;
                for (int i = 0; i < n; i++)
                {
                    x1[i] = x0[i] + alphak * p0[i];
                    r1[i] = r0[i] - alphak * temp2[i];
                }
                betak = MultiplyVectorByVector(r1, r1) / MultiplyVectorByVector(r0, r0);
                for (int i = 0; i < n; i++)
                {
                    p1[i] = r1[i] + betak * p0[i];
                }
                p0 = p1;
                r0 = r1;
                x0 = x1;
                k++;
            }
            return x1;
        }
        public static double[] ConjugateGradientMethodHMatrix(Supermatrix a, double[] b, double[] x0)
        {
            int n = b.Length;
            double[] x1 = new double[n];
            double[] temp = BlockClusterTree.MultHMatrixByVector(a, x0);
            double[] r0 = new double[n];
            double[] p0 = new double[n];
            double[] r1 = new double[n];
            double[] p1 = new double[n];
            double alphak = 0;
            double betak = 0;
            for (int i = 0; i < n; i++)
            {
                r0[i] = b[i] - temp[i];
            }
            p0 = r0;
            int k = 0;
            while (k != n)
            {
                double temp1 = MultiplyVectorByVector(r0, r0);
                double[] temp2 = BlockClusterTree.MultHMatrixByVector(a, p0);
                double temp3 = MultiplyVectorByVector(temp2, p0);
                alphak = temp1 / temp3;
                for (int i = 0; i < n; i++)
                {
                    x1[i] = x0[i] + alphak * p0[i];
                    r1[i] = r0[i] - alphak * temp2[i];
                }
                betak = MultiplyVectorByVector(r1, r1) / MultiplyVectorByVector(r0, r0);
                for (int i = 0; i < n; i++)
                {
                    p1[i] = r1[i] + betak * p0[i];
                }
                p0 = p1;
                r0 = r1;
                x0 = x1;
                k++;
            }
            return x1;
        }
    }
}
