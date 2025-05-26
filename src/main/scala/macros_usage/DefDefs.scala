package macros_usage

import macros.DefDefs.generateDynamicFunction

object DefDefs {
  /*
  Synthetic code:
  {
    def myFunction[A <: Int](int: A, string: String, boolean: Boolean) = { 
      if (boolean) int else string.length
    }

    myFunction(theInt, theString, theBoolean)
  }
  */
  val dynamicFunctionApplication = generateDynamicFunction(3, "Scala", true)

  def main(a: Array[String]) =
    println(dynamicFunctionApplication)
}
