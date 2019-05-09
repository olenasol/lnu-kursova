
fun Factorial(x: Int): Int {
    return if (x <= 0) {
        1
    } else {
        x * Factorial(x - 1)
    }
}

fun DerivativeXg(x: Double, y: Double, v: Int): Double {
    return Math.pow(-1.0, (v - 1).toDouble()) * Factorial(v - 1).toDouble() * Math.pow(x - y, -v.toDouble())
}

fun gTulda(x: Double, y: Double, k: Int, a: Double, b: Double): Double {
    val x0 = 0.5 * (a + b)
    var s = 0.0
    for (v in 0..(k - 1)) {
        s +=  1.0 / Factorial(v).toDouble() * DerivativeXg(x0, y, v) * Math.pow(x - x0, v.toDouble())
    }
    return s
}

fun f(x: Double, a: Double): Double {
    return a * Math.log(Math.abs(x - a)) - x * Math.log(Math.abs(x - a)) - a
}

fun f1(x: Double, a: Double): Double {
    return 0.5 * (a * Math.signum(x-a) * (Math.log(Math.abs(x - a)) - Math.log(Math.abs(a - x))) + a * Math.log(Math.abs(x - a)) + a * Math.log(Math.abs(a - x)) - 2 * x * Math.log(Math.abs(a - x)) - 2 * a)
    //return a * (x - a) * Math.Log(x - a) - a * x;
}

fun f2(x: Double, a: Double): Double {
    return 0.25 * (x - a) * (2 * (a + x) * Math.log(Math.abs(x - a)) - 3 * a - x) + a * x
}

fun fIntegr(c: Double, d: Double, a: Double): Double {
    return f1(d, a) - f1(c, a) - (f2(d, a) - f2(c, a))
}

fun Egtulda(i: Int, j: Int, n: Int): Double {
    val b = (i + 1.0) / n.toDouble()
    val a = i.toDouble() / n.toDouble()
    val d = (j + 1.0) / n.toDouble()
    val c = j.toDouble() / n.toDouble()
    val eps = 0.00001
    val k = -Integral.RegtangleMethod({ l, m -> f(l, m) }, i.toDouble() / n.toDouble(), (i + 1.0) / n.toDouble(), eps, c)
    val m = Integral.RegtangleMethod({ l, m -> f(l, m) }, i.toDouble() / n.toDouble(), (i + 1.0) / n.toDouble(), eps, d)
    //double k = -fIntegr(c,d, a);
    //double m = fIntegr(c,d, b);
    return k + m
}

fun phi(i: Int, n: Int, x: Double): Double {
    return if (x >= i.toDouble() / n.toDouble() && x <= (i + 1).toDouble() / n.toDouble()) {
        1.0
    } else {
        0.0
    }
}

fun amatrIntegr(x: Double, x0: Double, v: Int): Double {
    return Math.pow((x - x0), (v + 1).toDouble()) / (v + 1).toDouble()
}

fun amatr(i: Int, n: Int, x0: Double, v: Int): Double {
    return amatrIntegr((i + 1.0) / n.toDouble(), x0, v) - amatrIntegr(i.toDouble() / n.toDouble(), x0, v)
    // return Math.Pow((((double)(i + 1.0)) / ((double)n)) - x0, v + 1.0) / ((double)(v + 1.0)) - Math.Pow(((double)i) /((double) n) - x0, v + 1.0) / ((double)(v + 1.0));
}

fun bmatr1(j: Int, n: Int, x0: Double, v: Int): Double {
    return Math.pow(-1.0, (v + 1).toDouble()) * Math.pow(v.toDouble(), -1.0) * (bmatr1Integr((j + 1.0) / n.toDouble(), x0, v) - bmatr1Integr(j.toDouble() / n.toDouble(), x0, v))
    //  return Math.Pow(-1.0, v + 1.0) * Math.Pow(v, -1.0) * (Math.Pow(x0 - (((double)(j + 1.0)) / ((double)n)), 1.0 - v) / ((double)(1.0 - v))
    //      - Math.Pow(x0 - ((double)j) / ((double)n), 1.0 - v) / ((double)(1.0 - v)));
}

fun bmatr1Integr(x: Double, x0: Double, v: Int): Double {
    return if (v == 1) {
        -Math.log(Math.abs(x0 - x))
    } else {
        Math.pow(x0 - x, (1 - v).toDouble()) / (v.toDouble() - 1)
    }
}

fun bmatr2(j: Int, n: Int, x0: Double, v: Int): Double {
    // return ((((double)(j + 1.0)) / ((double)n)) * Math.Log(Math.Abs(x0 - ((double)(j + 1.0) / ((double)n))), Math.E) - x0 * Math.Log(Math.Abs((((double)(j + 1)) / ((double)n)) - x0),Math.E) - (((double)(j + 1)) / ((double)n))) -
    //     ((((double)j) / ((double)n)) * Math.Log(Math.Abs(x0 - (((double)j) / ((double)n))), Math.E) - x0 * Math.Log(Math.Abs((((double)j) / ((double)n)) - x0),Math.E) - (((double)j) / ((double)n)));
    return bmatr2Integr((j + 1.0) / n.toDouble(), x0) - bmatr2Integr(j.toDouble() / n.toDouble(), x0)
}

private fun bmatr2Integr(x: Double, x0: Double): Double {
    return 0.5 * (x * Math.signum(x0 - x) * (Math.log(Math.abs(x0 - x)) - Math.log(Math.abs(x - x0))) + x * Math.log(Math.abs(x0 - x)) + x * Math.log(Math.abs(x - x0)) - 2 * x0 * Math.log(Math.abs(x - x0)) - 2 * x)
}