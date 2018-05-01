using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Methods
{
    public class Integral
    {
        public delegate double Func(double x);
        public delegate double Func1(double x, double y);

        static public double RegtangleMethod(Func f,double a,double b,double eps)
        {
            int n0 = 100;
            int n1 = 2*n0;
            while(Math.Abs(RegtMethod(f,a,b,n1)-RegtMethod(f,a,b,n0))/(Math.Abs(RegtMethod(f,a,b,n1)))>eps)
            {
                n0 = n1;
                n1 = 2 * n0;
            }
            return RegtMethod(f, a, b, n1);
        }

        static private double RegtMethod(Func f,double a,double b,int n)
        {
            double h, S, x;
            h = ((double)(b - a)) / ((double)n);
            S = 0;
            for (int i = 1; i <= n; i++)
            {
                x = a + ((double)(2*i-1) * h)/2.0;
                S = S + f(x);
            }
            S = h * S;
            return S;
        }
        static public double TrapezeMethod(Func f, double a, double b, double eps)
        {
            int n0 = 100;
            int n1 = 2 * n0;
            while (Math.Abs(TrapMethod(f, a, b, n1) - TrapMethod(f, a, b, n0)) > eps)
            {
                n0 = n1;
                n1 = 2 * n0;
            }
            return TrapMethod(f, a, b, n1);
        }
        static private double TrapMethod(Func f, double a, double b,int n)
        {
            double h, S, x;
            h = ((double)(b - a)) / ((double)n);
            S = 0;
            for (int i = 0; i < n; i++)
            {
                x = a + ((double)i * h);
                if (i >= 1)
                {
                    S = S + 2.0 * f(x);
                }
                else
                {
                    S = S + f(x);
                }
            }
            S = ((double)(b - a)) / ((double)2 * n) *( S + f(b));
            return S;
        }
        static public double TrapezeMethod(Func1 f, double a, double b, double eps,double t)
        {
            int n0 = 100;
            int n1 = 2 * n0;
            while (Math.Abs(TrapMethod(f, a, b, n1,t) - TrapMethod(f, a, b, n0,t)) > eps)
            {
                n0 = n1;
                n1 = 2 * n0;
            }
            return TrapMethod(f, a, b, n1,t);
        }
        static private double TrapMethod(Func1 f, double a, double b, int n,double t)
        {
            double h, S, x;
            h = ((double)(b - a)) / ((double)n);
            S = 0;
            for (int i = 0; i < n; i++)
            {
                x = a + ((double)i * h);
                if (i >= 1)
                {
                    S = S + 2.0 * f(t,x);
                }
                else
                {
                    S = S + f(t,x);
                }
            }
            S = ((double)(b - a)) / ((double)2 * n) * (S + f(t,b));
            return S;
        }

        static public double RegtangleMethod(Func1 f, double a, double b, double eps,double t)
        {
            int n0 = 100;
            int n1 = 2 * n0;
            while (Math.Abs(RegtMethod(f, a, b, n1,t) - RegtMethod(f, a, b, n0,t))  > eps)
            {
                n0 = n1;
                n1 = 2 * n0;
            }
            return RegtMethod(f, a, b, n1,t);
        }

        static private double RegtMethod(Func1 f, double a, double b, int n,double t)
        {
            double h, S, x;
            h = ((double)(b - a)) / ((double)n);
            S = 0;
            for (int i = 1; i <= n; i++)
            {
                x = a + ((double)(2 * i - 1) * h) / 2.0;
                S = S + f(t,x);
            }
            S = h * S;
            return S;
        }
        
    }
}
