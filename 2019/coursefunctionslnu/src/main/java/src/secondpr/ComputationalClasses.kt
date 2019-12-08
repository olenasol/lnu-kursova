package src.secondpr


data class Supermatrix(var rows:Int = 0, var cols:Int = 0,
                       var blockrows:Int=0, var blockcols:Int =0,
                       var sLeaf:List<Int> , var tLeaf: List<Int> ,
                       var rkmatrix:Rkmatrix?=null, var fullmatrix: Fullmatrix?=null, var supermatrix: Array<Array<Supermatrix>>?=null)

data class Rkmatrix(val m:Int, val rows:Int, val cols:Int,
                    var sLeaf:List<Int>, var tLeaf: List<Int> ,
                    var a:Array<DoubleArray> = Array(rows){
                        DoubleArray((m+1)*(m+1))
                    },
                    var b:Array<DoubleArray> = Array(cols){
                        DoubleArray((m+1)*(m+1))
                    })

data class Fullmatrix(var rows:Int=0, var cols:Int=0,
                      var sLeaf:List<Int>, var tLeaf: List<Int> ,
                      var e:Array<DoubleArray> = Array(rows){DoubleArray(cols)})