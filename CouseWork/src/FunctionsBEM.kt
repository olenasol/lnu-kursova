
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
    return bmatr2Integr(x,a)//a * Math.log(Math.abs(x - a)) - x * Math.log(Math.abs(x - a)) - a
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
//TODO wtf with integral - argument below 0
fun Egtulda(i: Int, j: Int, n: Int): Double {
    val b = (i + 1.0) / n.toDouble()
    val a = i.toDouble() / n.toDouble()
    val d = (j + 1.0) / n.toDouble()
    val c = j.toDouble() / n.toDouble()
//    var k = 0.0
//    var k2 = 0.0
//    var m = 0.0
//    var m2 = 0.0
//    if ((b-c) >0.0) {
//        k = 0.25 * Math.pow(b - c, 2.0) * (2 * Math.log(b - c) - 1)
//    }
//    k+=-0.5*Math.pow(b,2.0)+c*b
//    if ((a-c)>0.0)
//        k2 = - 0.25*Math.pow(a-c,2.0)*(2*Math.log(a-c)-1)
//    k2+=0.5*Math.pow(a,2.0)-c*a
//    if((d-b)> 0.0)
//        m =  -0.25*Math.pow(d-b,2.0)*(2*Math.log(d-b)-1)
//    m+=0.5*Math.pow(b,2.0)-d*b
//    if((d-a)> 0.0)
//        m2 = 0.25*Math.pow(d-a,2.0)*(2*Math.log(d-a)-1)
//    m2+=- 0.5*Math.pow(a,2.0)+d*a
//    println("testing eg "+" "+a+" "+b+" "+c+" "+d+" "+k+" "+k2+"  "+m+" "+m2)
    if (i==j)
        return -Math.pow(b-a,2.0)*(Math.log(b-a)+0.5)
    else
        if(a<c){
            var sum =0.0
            if((d-b)!= 0.0)
                sum+=-0.25*Math.pow((d-b),2.0)*(2*Math.log(d-b)-1)
            if ((c-b)!= 0.0)
                sum+=0.25*Math.pow(c-b,2.0)*(2*Math.log(c-b)-1)
            sum += c*b-d*b
            if((d-a)!= 0.0)
                sum+=0.25*Math.pow((d-a),2.0)* (2*Math.log(d-a)-1)
            if((c-a)!= 0.0)
                sum+=-0.25*Math.pow(c-a,2.0)*(2*Math.log(c-a)-1)
            sum+=-c*a+d*a
            return sum
        } else{
            var sum =0.0
            if ((b-c)!= 0.0)
                sum+=0.25*Math.pow((b-c),2.0)*(2*Math.log(b-c)-1)
            if((b-d)!= 0.0)
                sum+= -0.25*Math.pow((b-d),2.0)*(2*Math.log(b-d)-1)
            sum+=d*b+c*b
            if((a-c)!=0.0)
                sum+=-0.25*Math.pow((a-c),2.0)*(2*Math.log(a-c)-1)
            if((a-d)!=0.0)
                sum+=0.25*Math.pow((a-d),2.0)*(2*Math.log(a-d)-1)
            sum +=-d*a-c*a
            return sum
        }
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
    return amatrIntegr((i.toDouble() + 1.0) / n.toDouble(), x0, v) - amatrIntegr(i.toDouble() / n.toDouble(), x0, v)
}

fun bmatr1(j: Int, n: Int, x0: Double, v: Int): Double {
    return Math.pow(-1.0, (v + 1).toDouble()) * Math.pow(v.toDouble(), -1.0) * (bmatr1Integr((j + 1.0) / n.toDouble(), x0, v) - bmatr1Integr(j.toDouble() / n.toDouble(), x0, v))
}

fun bmatr1Integr(x: Double, x0: Double, v: Int): Double {
    return if (v == 1) {
        -Math.log(Math.abs(x0 - x))
    } else {
        Math.pow(x0 - x, (1 - v).toDouble()) / (v.toDouble() - 1)
    }
}

fun bmatr2(j: Int, n: Int, x0: Double, v: Int): Double {
    return bmatr2Integr((j + 1.0) / n.toDouble(), x0) - bmatr2Integr(j.toDouble() / n.toDouble(), x0)
}

private fun bmatr2Integr(y: Double, x0: Double): Double {
    if(y < x0){
        return -((x0-y)*Math.log(x0-y)-(x0-y))
    }else{
        return (y-x0)*Math.log(y-x0) - (y-x0)
    }
}
fun getPohubka(a:DoubleArray,b:DoubleArray):Double{
    val c = DoubleArray(a.size)
    for(i in 0 until a.size){
        c[i] = Math.abs(a[i]-b[i])
    }
    var max = c[0]
    for (i in 1 until c.size){
        if (c[i]>max)
            max = c[i]
    }
    return max
}