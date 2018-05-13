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
            return a*Math.Log(Math.Abs(x-a))-x*Math.Log(Math.Abs(x-a))-a;
        }

        static public double f1(double x,double a)
        {
            return 0.5 * (a * Math.Sign(x - a) * (Math.Log(Math.Abs(x - a)) - Math.Log(Math.Abs(a - x))) + a * Math.Log(Math.Abs(x - a)) + a * Math.Log(Math.Abs(a - x)) - 2 * x * Math.Log(Math.Abs(a - x)) - 2 * a);
            //return a * (x - a) * Math.Log(x - a) - a * x;
        }
        static public double f2(double x, double a)
        {
            return 0.25 * (x - a) * (2 * (a + x) * Math.Log(Math.Abs(x - a)) - 3 * a - x) + a * x;
        }
        static public double fIntegr(double c,double d,double a)
        {
            return (f1(d, a) - f1(c, a)) - (f2(d, a) - f2(c, a));
        }
        static public double Egtulda(int i, int j, int n)
        {
            double b = ((double)(i + 1.0)) / (double)n;
            double a = ((double)i) / ((double)n);
            double d = ((double)(j + 1.0)) / (double)n;
            double c = ((double)j) / ((double)n);
            double eps = 0.00001;
            double k = -Integral.RegtangleMethod(f, ((double)i) / ((double)n), ((double)(i + 1.0)) / ((double)n), eps, c);
            double m=Integral.RegtangleMethod(f, ((double)i) /((double) n), ((double)(i + 1.0)) / ((double)n), eps, d);
            //double k = -fIntegr(c,d, a);
            //double m = fIntegr(c,d, b);
            return k+m;
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
        public static double amatrIntegr(double x, double x0, int v)
        {
            return Math.Pow(x - x0, v + 1) / (double)(v + 1);
        }

        public static double amatr(int i, int n, double x0,int v){
            return amatrIntegr(((double)(i + 1.0)) / ((double)n), x0, v) - amatrIntegr(((double)(i)) / ((double)n), x0, v);
           // return Math.Pow((((double)(i + 1.0)) / ((double)n)) - x0, v + 1.0) / ((double)(v + 1.0)) - Math.Pow(((double)i) /((double) n) - x0, v + 1.0) / ((double)(v + 1.0));
        }

        public static double bmatr1(int j, int n, double x0, int v)
        {
            return Math.Pow(-1, v + 1) * Math.Pow(v, -1) * (bmatr1Integr((double)(j + 1.0) / (double)n, x0, v) - bmatr1Integr((double)(j) / (double)n, x0, v));
          //  return Math.Pow(-1.0, v + 1.0) * Math.Pow(v, -1.0) * (Math.Pow(x0 - (((double)(j + 1.0)) / ((double)n)), 1.0 - v) / ((double)(1.0 - v))
          //      - Math.Pow(x0 - ((double)j) / ((double)n), 1.0 - v) / ((double)(1.0 - v)));
        }

        public static double bmatr1Integr(double x, double x0, int v)
        {
            if (v == 1)
            {
                return -Math.Log(Math.Abs(x0 - x));
            }
            else
            {
                return Math.Pow(x0 - x, 1 - v) / ((double)v - 1);
            }
        }

        public static double bmatr2(int j, int n, double x0, int v)
        {
            // return ((((double)(j + 1.0)) / ((double)n)) * Math.Log(Math.Abs(x0 - ((double)(j + 1.0) / ((double)n))), Math.E) - x0 * Math.Log(Math.Abs((((double)(j + 1)) / ((double)n)) - x0),Math.E) - (((double)(j + 1)) / ((double)n))) - 
            //     ((((double)j) / ((double)n)) * Math.Log(Math.Abs(x0 - (((double)j) / ((double)n))), Math.E) - x0 * Math.Log(Math.Abs((((double)j) / ((double)n)) - x0),Math.E) - (((double)j) / ((double)n)));
            return bmatr2Integr(((double)(j + 1.0)) / ((double)n), x0) - bmatr2Integr(((double)(j)) / ((double)n), x0);
        }

        private static double bmatr2Integr(double x,double x0)
        {
            return 0.5 * (x * Math.Sign(x0 - x) * (Math.Log(Math.Abs(x0 - x)) - Math.Log(Math.Abs(x - x0))) + x * Math.Log(Math.Abs(x0 - x)) + x * Math.Log(Math.Abs(x - x0)) - 2 * x0 * Math.Log(Math.Abs(x - x0)) - 2 * x);
        }
    }
}
