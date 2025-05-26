package macros_usage

import macros.Summoning.*

object Summoning {
  given MyTypeClass[String] with
    def message: String = "String descriptor"

  val aTupleDescriptor = describeType[(Int, String, Boolean)]
//  val wrongTupleDescriptor = describeType[(Int, Int, String)] // error with the custom message
}
