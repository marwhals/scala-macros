package macros

import scala.quoted.*

// trait Expr // "code element" = an AST (Abstract Syntax Tree)
// case class Num(value: Double) extends Expr
// case class Sum(left: Expr, right: Expr) extends Expr
// case class Div(left: Expr, right: Expr) extends Expr
// // same for all ops
// case class Sin(expr: Expr) extends Expr

object MacroIntro {
  // metaprogramming = programming with "code elements" as first class values, i.e manipulate code at compile time
  // Compiler: Code => AST => Bytecode / Binary => run that binary
  // code -> AST (Quoting) -> New Code (Splicing) -> Compiled Later
  // ^^^^^^^^^^^^^a macro^^^^^^^^^^^^^^^^^^^^^^^^^^


  /*
    Macro structure
    - inline function with some args
    - args/expressions can be QUOTED => turned into ASTs, as Expr[A]
    - those ASTs are manipulated into some other AST, as Expr[B]
    - that final AST is injected into the code (= SPLICED) => a final value is returned
   */
  inline def firstMacro(number: Int, string: String): String =
    ${ firstMacroImpl('number, 'string) } // ${ AST } = splicing that AST

  // macro implementation = manipulating ASTs
  // this runs at compile time
  def firstMacroImpl(numAST: Expr[Int], stringAST: Expr[String])(using Quotes): Expr[String] = {
    // Expr[A] can be turned into a value if it's known at compile time
    val numValue = numAST.valueOrAbort // triggers a compile error if it's not a constant
    val stringValue = stringAST.valueOrAbort

    // expressions can be evaluated at compile time
    // you have access to the Scala standard library
    val newString =
      if (stringValue.length > 10) stringValue.take(numValue)
      else stringValue.repeat(numValue)

    Expr("This macro impl is: " + newString) // can build Exprs manually

  }

  // macro with inline arguments
  inline def firstMacroIA(inline number: Int, inline string: String): String =
    ${ firstMacroIAImpl('number, 'string) } // quoting an inline arg actually expands the entire expression, can only run basic computations (e.g. arithmetic or string concatenation)

  def firstMacroIAImpl(numAST: Expr[Int], stringAST: Expr[String])(using Quotes): Expr[String] = {
    Expr("The number: " + numAST.show + " and the string: " + stringAST.show)
  }


}
