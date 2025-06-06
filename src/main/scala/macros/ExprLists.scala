package macros

import quoted.*

object ExprLists {
  inline def processVarargs(inline values: Int*) =
    ${ processVarargsImpl('values) }

  def processVarargsImpl(values: Expr[Seq[Int]])(using Quotes): Expr[String] =
    values match {
      case Varargs(elems) => // Seq[Expr[Int]] ---> Unwrap the varargs types and manipulate the individual values
        val nums = elems.map(_.show)
        val numExprs = Expr(nums.toString) // show the sequence of argument expressions
        '{ "I just received: " + $numExprs }

      case _ => Expr("got something else")
    }



  //-------------
  inline def returnExprs() =
    ${ returnExprsImpl }

  def returnExprsImpl(using Quotes): Expr[List[String]] = {
    val exprs: List[Expr[String]] = // Build a bunch of expressions and put those expressions in a list 😅
      List('{ "Scala" * 2 }, '{ "macros".toUpperCase() })

    // can turn a List[Expr[String]] => Expr[List[String]]
    val finalExpr = Expr.ofList(exprs)
    // equivalent: Varargs(exprs)

    finalExpr
  }

}
