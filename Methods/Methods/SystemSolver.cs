using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Methods
{
    public class SystemSolver
    {
        static public double[] MetodHausa(int n, double[,] a, double[] b)
        {
            double[] x = new double[n];
            double[,] m = new double[n, n];
            int p = 0; double temp;
            for (int k = 0; k <= n - 2; k++)
            {
                for (int l = k + 1; l <= n - 1; l++)
                {
                    if (Math.Abs(a[l, k]) > Math.Abs(a[k, k]))
                    {
                        for (int t = 0; t <= n - 1; t++)
                        {
                            temp = a[k, t];
                            a[k, t] = a[l, t];
                            a[l, t] = temp;
                        }
                        temp = b[k];
                        b[k] = b[l];
                        b[l] = temp;
                        p = p + 1;
                    }
                }
                for (int i = k + 1; i <= n - 1; i++)
                {
                    m[i, k] = -(a[i, k] / a[k, k]);
                    b[i] = b[i] + m[i, k] * b[k];
                    for (int j = k; j <= n - 1; j++)
                    {
                        a[i, j] = a[i, j] + (m[i, k] * a[k, j]);
                    }
                }
            }
            x[n - 1] = b[n - 1] / a[n - 1, n - 1];
            double s;
            for (int k = n - 2; k >= 0; k--)
            {
                s = 0;
                for (int j = k + 1; j <= n - 1; j++)
                {
                    s = s + a[k, j] * x[j];
                }
                x[k] = (b[k] - s) / a[k, k];
            }
            return x;
        }

        static public void LUMatrix(double[,] a,out double[,] l,out double[,] u)
        {
            int n = a.GetLength(0);
            l = new double[n,n];
            u = new double[n,n];
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (i == j)
                    {
                        l[i,j] = 1;
                    }
                    else
                    {
                        l[i,j] = 0;
                    }
                }
            }
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    u[i,j] = 0;
                }
            }
            double s;
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    if (i <= j)
                    {
                        s = 0;
                        for (int k = 0; k <= i - 1; k++)
                        {
                            s = s + l[i,k] * u[k,j];
                        }
                        u[i,j] = a[i,j] - s;
                    }
                    else
                    {
                        s = 0;
                        for (int k = 0; k <= j - 1; k++)
                        {
                            s = s + l[i,k] * u[k,j];
                        }
                        l[i,j] = (a[i,j] - s) / u[j,j];
                    }
                }
            }
        }

        static public double[] LURozklad(double[,] a,double[] b)
        {
            double[,] l;
            double[,] u;
            double s;
            int n = a.GetLength(0);
            double[] y = new double[n];
            double[] x = new double[n];
            SystemSolver.LUMatrix(a, out l, out u);
            for (int i = 0; i < n; i++)
            {
                s = 0;
                for (int k = 0; k <= i - 1; k++)
                {
                    s = s + l[i,k] * y[k];
                }
                y[i] = b[i] - s;
            }
                
            for (int i = n - 1; i >= 0; i--)
            {
                s = 0;
                for (int k = i + 1; k < n; k++)
                {
                    s = s + u[i,k] * x[k];
                }
                x[i] = (y[i] - s) / u[i,i];
            }
            return x;           
        }

    }
}
