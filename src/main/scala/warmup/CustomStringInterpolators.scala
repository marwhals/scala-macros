package warmup


object CustomStringInterpolators {

  // s-interpolator
  val pi = 3.14159
  val sInterpolator = s"The value of PI is appox ${pi + 0.000002}, the regular pi is $pi"

  // f-interpolator, similar to printf
  val fInterpolator = f"The value of PI up to 3 sig digits is $pi%3.2f"

  // raw-interpolator = escape sequences
  val rawInterpolator = raw"The value of pi is $pi\n this is NOT a newline"

  case class Person(name: String, age: Int)

  def string2Person(line: String): Person = {
    val tokens = line.split(",")
    Person(tokens(0), tokens(1).toInt)
  }

  extension(sc: StringContext) //Extension method ---- add a method after the type has been defined
    def pers(args: Any*): Person = {
      val concat = sc.s(args*) // using the known s-interpolator
      string2Person(concat)
    }

  def main(args: Array[String]): Unit = {
    println(sInterpolator)
    println(fInterpolator)
    println(rawInterpolator)
  }
}
