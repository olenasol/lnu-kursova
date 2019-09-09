
class Util{
    companion object {
        fun log2(x: Int): Int {
            return (Math.log(x.toDouble()) / Math.log(2.0)).toInt()
        }
    }
}