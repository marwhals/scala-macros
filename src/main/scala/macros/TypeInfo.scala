package macros

import scala.quoted.*

object TypeInfo {
  inline def myLittleMacro(x: Int): Int = ${ myLittleMacro('x)}

  def myLittleMacro(x: Expr[Int])(using Quotes): Expr[Int] = {
    // type instance, synthesized by the compiler in this scope
    // only available inside a macro implementation
    val intType: Type[Int] = Type.of[Int] //instance describing a type
    // allows the macro impl to make a decision

    // this type info is available BEFORE type erasure
    val listIntTypeDescription: String = Type.show[List[Int]]

    Expr(42) // value not important

  }
}
