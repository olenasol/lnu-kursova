import kotlin.math.abs
import kotlin.math.ln

fun funderint(a:Double,b:Double):Double{
    var res = 0.0
    if(b!= 0.0)
        res+=Math.pow(b,2.0)*ln(abs(b))
    if((b-1)!=0.0)
        res += -ln(Math.abs(b-1))*Math.pow(b-1,2.0)
    res+=-3*b
    if(a!=0.0)
        res+=-Math.pow(a,2.0)*ln(abs(a))
    if((a-1)!=0.0){
        res+=ln(abs(a-1))*Math.pow((a-1),2.0)
    }
    res+=3*a
    return res/2.0
}
fun Egtulda(i: Int, j: Int, n: Int): Double {
    val b = (i + 1.0) / n.toDouble()
    val a = i.toDouble() / n.toDouble()
    val d = (j + 1.0) / n.toDouble()
    val c = j.toDouble() / n.toDouble()
    var sum =0.0
    if((d-b)!= 0.0){
        sum+=-0.5*Math.pow((d-b),2.0)*Math.log(Math.abs(d-b))
    }
    if((d-a)!=0.0){
        sum+=0.5*Math.pow((d-a),2.0)*Math.log(Math.abs(d-a))
    }
    sum+=-0.25*(2*b-2*a)*d
    if((c-b)!=0.0){
        sum+=0.5*Math.pow((c-b),2.0)*Math.log(Math.abs(c-b))
    }
    if((c-a)!=0.0){
        sum+=-0.5*Math.pow((c-a),2.0)*Math.log(Math.abs(c-a))
    }
    sum+=0.25*(2*b-2*a)*c
    sum+=-d*(b-a)+c*(b-a)
    return sum
//    if (i==j)
//        return 0.5*Math.pow(b-a,2.0)*(2*Math.log(b-a)-1)-Math.pow(b-a,2.0)
//    else
//        if(a<c){
//            var sum =0.0
//            if((d-b)!= 0.0)
//                sum+=-0.25*Math.pow((d-b),2.0)*(2*Math.log(d-b)-1)
//            if ((c-b)!= 0.0)
//                sum+=0.25*Math.pow(c-b,2.0)*(2*Math.log(c-b)-1)
//            sum += c*b-d*b
//            if((d-a)!= 0.0)
//                sum+=0.25*Math.pow((d-a),2.0)* (2*Math.log(d-a)-1)
//            if((c-a)!= 0.0)
//                sum+=-0.25*Math.pow(c-a,2.0)*(2*Math.log(c-a)-1)
//            sum+=-c*a+d*a
//            return sum
//        } else{
//            var sum =0.0
//            if ((b-c)!= 0.0)
//                sum+=0.25*Math.pow((b-c),2.0)*(2*Math.log(b-c)-1)
//            if((b-d)!= 0.0)
//                sum+= -0.25*Math.pow((b-d),2.0)*(2*Math.log(b-d)-1)
//            sum+=-d*b+c*b
//            if((a-c)!=0.0)
//                sum+=-0.25*Math.pow((a-c),2.0)*(2*Math.log(a-c)-1)
//            if((a-d)!=0.0)
//                sum+=0.25*Math.pow((a-d),2.0)*(2*Math.log(a-d)-1)
//            sum +=d*a-c*a
//            return sum
//        }
}

fun phi(i: Int, n: Int, x: Double): Double {
    return if (x >= i.toDouble() / n.toDouble() && x < (i + 1).toDouble() / n.toDouble()) {
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