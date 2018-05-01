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
            if (x <= 0)
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
                s = s + (1.0 / (double)Factorial(v)) * DerivativeXg(x0, y, v) * Math.Pow(x - x0, v);
            }
            return s;
        }

        static public double f(double x,double a){
            return a*Math.Log(x-a)-x*Math.Log(x-a)-a;
        }

        static public double Egtulda(int i, int j, int n)
        {
            double b = ((double)(j + 1.0)) / (double)n;
            double a = ((double)j) / ((double)n);
            //if (a == 0)
            //{
            //    a = 0.0001;
            //}
            //if (b == 0) { b = 0.0001;}
            double eps = 0.00001;
            double k = -Integral.RegtangleMethod(f, ((double)i) / ((double)n), ((double)(i + 1.0)) / ((double)n), eps, a);
            double m=Integral.RegtangleMethod(f, ((double)i) /((double) n), ((double)(i + 1.0)) / ((double)n), eps, b);
            return k + m;
        }

        public static double phi(int i, int n, double x)
        {
            if ((x >= ((double)i / (double)n)) && (x <= ((double)(i + 1) /(double) n)))
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }

        public static double amatr(int i, int n, double x0,int v){
            return Math.Pow((((double)(i + 1.0)) / ((double)n)) - x0, v + 1.0) / ((double)(v + 1.0)) - Math.Pow(((double)i) /((double) n) - x0, v + 1.0) / ((double)(v + 1.0));
        }

        public static double bmatr1(int j, int n, double x0, int v)
        {
            return Math.Pow(-1.0, v + 1.0) * Math.Pow(v, -1.0) * (Math.Pow(x0 - (((double)(j + 1.0)) / ((double)n)), 1.0 - v) / ((double)(1.0 - v))
                - Math.Pow(x0 - ((double)j) / ((double)n), 1.0 - v) / ((double)(1.0 - v)));
        }

        public static double bmatr2(int j, int n, double x0, int v)
        {
            return ((((double)(j + 1.0)) / ((double)n)) * Math.Log(x0 - ((double)(j + 1.0) / ((double)n)), Math.E) - x0 * Math.Log((((double)(j + 1)) / ((double)n)) - x0,Math.E) - (((double)(j + 1)) / ((double)n))) - 
                ((((double)j) / ((double)n)) * Math.Log(x0 - (((double)j) / ((double)n)), Math.E) - x0 * Math.Log((((double)j) / ((double)n)) - x0,Math.E) - (((double)j) / ((double)n)));
        }
    }
}
