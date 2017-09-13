using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using KursovaLibrary;
using Methods;

namespace Kursova_Console
{
    class Program
    {
        static public double f_i(double x,double i)
        {
            
            return x;
        }
        static public double un(double x, double[] u, int n)
        {
            double s = 0;
            for (int i = 0; i < n; i++)
            {
               
                s = s + FunctionsBEM.phi(i, n,x) * u[i];
            }
          
            return s;
        }
        static void Main(string[] args)
        {
            Supermatrix s = new Supermatrix();
            ClusterTree c=ClusterTree.buildClusterTree(8);
            s = BlockClusterTree.BuildBlockClusterTree(c, c, s,8);
            double[] f = new double[8];
            double[] x0=new double[8];
            for (int i = 0; i < 8; i++)
            {
                f[i] = f_i(i+0.125,i);
                //Console.WriteLine(f[i]);
                x0[i]=0;
            }

            double[] u = GradientMethod.ConjugateGradientMethodHMatrix(s, f, x0);
            for (int i = 0; i < 8; i++)
            {
                //Console.WriteLine(u[i]);
            }
            double[] su = new double[8];
            su[0] = un(0, u, 8);
            su[1] = un(0.2, u, 8);
            su[2] = un(0.4, u, 8);
            su[3] = un(0.6, u, 8);
            su[4] = un(0.8, u, 8);
            su[5] = un(0.9, u, 8);
            su[6] = un(0.1, u, 8);
            su[7] = un(1, u, 8);
            for (int i = 0; i < 8; i++)
                    {
                        
                      Console.WriteLine(su[i]);
                    }
                //double[,] a = new double[3,3];
                //double[] b = {11,16,14};
                //a[0, 0] = 1;
                //a[0, 1] = 7;
                //a[0, 2] = 3;
                //a[1, 0] = 7;
                //a[1, 1] = 4;
                //a[1, 2] = 5;
                //a[2, 0] = 3;
                //a[2, 1] = 5;
                //a[2, 2] = 6;
                //double[] x0={0,0,0};
                //double[] x = GradientMethod.ConjugateGradientMethod(a, b, x0);
                
        }
    }
}
