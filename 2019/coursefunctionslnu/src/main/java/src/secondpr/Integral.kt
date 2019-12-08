package src.secondpr

class Integral{
    companion object {
        public fun RegtangleMethod(f:(Double,Double)->Double,a:Double,b:Double,eps:Double,t:Double):Double
        {
            var n0 = 100
            var n1 = 2*n0
            while(Math.abs(RegtMethod(f,a,b,n1,t)-RegtMethod(f,a,b,n0,t))/(Math.abs(RegtMethod(f,a,b,n1,t)))>eps)
            {
                n0 = n1
                n1 = 2 * n0
            }
            return RegtMethod(f, a, b, n1,t)
        }

        private fun RegtMethod(f:(Double,Double)->Double,a:Double,b:Double,n:Int,t:Double):Double
        {
            val h = ((b - a).toDouble()) / (n.toDouble())
            var S = 0.0
            for (i in 1..n)//int i = 1; i <= n; i++)
            {
                val x = a + ((2*i-1).toDouble() * h)/2.0;
                S += f(x,t)
            }
            S *= h
            return S
        }
    }
}