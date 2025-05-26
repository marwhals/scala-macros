package macros_usage

import macros.TreeMappings.*

object TreeMappings {
  val scopedValue = transformCode {
    def multiply(x: String, y: Int) = x * y

    val mol = 42
    val fl = "Scala"

    println(s"The meaning of life is $mol and fav language is $fl")
  }

  val flippedBooleans = flipBooleans {
    val x = true //When this is run the value here will be false...etc for the rest of the program
    val y = 2 > 3
    val z = x && y

    def funcBool(a: Boolean, b: Boolean) = a && b

    if (z || false) funcBool(x, y)
    else false
  }
  
  // Useful for removing bad patterns/ warts in code 💡
  // List(1,2,3,4).filter(_ % 2 == 0).size => List(1,2,3).count(_ % 2 == 0)
  
  val gatheredStatements = demoAccumulator {
    val x = 1 + 3

    println(x) // this should gather the expression x

    val y = {
      println("hello, I'm writing Scala")
      x * 3
    }

    def logNumbers(a: Int, b: Int) = {
      println(a)
      println(b)
      a + b
    }

    println(logNumbers(x, y))
  }
}
