package macros_usage

object {
  val varargDescriptor = processVarargs(1 * 2 * 3, 3 + 45, 99)

  val listOfExpressions = returnExprs()
}
