
data class Supermatrix(var rows:Int = 0, var cols:Int = 0,
                       var blockrows:Int=0, var blockcols:Int =0,
                       var rkmatrix:Rkmatrix?=null, var fullmatrix: Fullmatrix?=null, var supermatrix: Array<Array<Supermatrix>>?=null)

data class Rkmatrix(val k:Int,val rows:Int,val cols:Int,
                    var a:Array<DoubleArray> = Array(rows){
                        DoubleArray(k)
                    },var b:Array<DoubleArray> = Array(cols){
                        DoubleArray(k)
                    })

data class Fullmatrix(var rows:Int=0, var cols:Int=0,
                      var e:DoubleArray= DoubleArray(1))