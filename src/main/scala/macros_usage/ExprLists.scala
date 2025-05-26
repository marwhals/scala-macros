package macros_usage

import macros.ExprLists.{processVarargs, returnExprs}

object ExprLists {
  val varargDescriptor = processVarargs(1 * 2 * 3, 3 + 45, 99)

  val listOfExpressions = returnExprs()
}
