using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Methods;

namespace KursovaLibrary
{
    public class FunctionsBEM
    {
        public static int Factorial(int x)
        {
            if (x == 0)
            {
                return 1;
            }
            else
            {
                return x * Factorial(x - 1);
            }
        }

        public static double DerivativeXg(double x, double y, int v)
        {
            return Math.Pow(-1, v - 1) * (double)Factorial(v - 1) * Math.Pow(x - y, -v);
        }

        public static double gTulda(double x, double y,int k,double a,double b)
        {
            double x0 = 0.5*(a+b);
            double s = 0;
            for (int v = 0; v <= k-1; v++)
            {
                s = s + (1 / Factorial(v)) * DerivativeXg(x0, y, v) * Math.Pow(x - x0, v);
            }
            return s;
        }
        static public double f(double x,double a){
            return a*Math.Log(x-a)-x*Math.Log(x-a)-a;
        }
        static public double Egtulda(int i, int j, int n)
        {
            double b = (double)(j + 1) / n;
            double a = (double)(j / n);
            if (a == 0)
            {
                a = 0.0001;
            }
            if (b == 0) { b = 0.0001;}
            double eps = 0.00001;
            double k = -Integral.RegtangleMethod(f, (double)(i / n), (double)((i + 1) / n), eps, a);
            double m=Integral.RegtangleMethod(f, (double)(i / n), (double)((i + 1) / n), eps, b);
            return k + m;
        }

        public static double phi(int i, int n, double x)
        {
            if ((x >= (double)(i / n)) && (x <= (double)((i + 1) / n)))
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
        public static double amatr(int i, int n, double x0,int v){
            return Math.Pow(((i + 1) / n) - x0, v + 1) / (v + 1) - Math.Pow((i / n) - x0, v + 1) / (v + 1);
        }
        public static double bmatr1(int j, int n, double x0, int v)
        {
            return Math.Pow(-1, v + 1) * Math.Pow(v, -1) * (Math.Pow(x0 - ((j + 1) / n), 1 - v) / (1 - v) - Math.Pow(x0 - (j / n), 1 - v) / (1 - v));
        }
        public static double bmatr2(int j, int n, double x0, int v)
        {
            return (((j + 1) / n) * Math.Log(x0 - ((j + 1) / n), Math.E) - x0 * Math.Log(((j + 1) / n) - x0,Math.E) - ((j + 1) / n)) - ((j / n) * Math.Log(x0 - (j / n), Math.E) - x0 * Math.Log((j / n) - x0,Math.E) - (j / n));
        }
    }
}
