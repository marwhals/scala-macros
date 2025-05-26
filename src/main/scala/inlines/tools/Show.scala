package inlines.tools

import scala.compiletime.*
import scala.deriving.Mirror

trait Show[A] {
  def show(a: A): String
}

object Show {
  given Show[String] with
    def show(a: String): String = a

  given Show[Int] with
    def show(a: Int): String = a.toString

  given Show[Boolean] with
    def show(a: Boolean): String = a.toString

  // auto-derivation for serialisation type class
  // showTuple[(String, Int, Boolean) , ("name", "age", "programmer")]("Bob", 99, true)
  // ["name: Bob", "age: 99]
  private inline def showTuple[E <: Tuple, L <: Tuple](elements: E): List[String] =
    inline (elements, erasedValue[L]) match { // (("Bob", 99, true), ("name", "age", "programmer"))
      case (EmptyTuple, EmptyTuple) => List()
      case (el: (eh *: et), lab: (lh *: lt)) =>
        val (h *: t) = el // h = "bob", t = (99, true)
        val label = constValue[lh] // label = "name"
        val value = summonInline[Show[eh]].show(h) // Show[String].show("Bob")

        ("" + label + ": " + value) :: showTuple[et, lt](t)
      // "name: Bob" :: showTuple[(Int, Boolean), ("age", "programmer")]((99, true))
    }

  /*
  - Necessary for type class derivation
   */
  inline def derived[A <: Product](using m: Mirror.ProductOf[A]): Show[A] = {
    new Show[A] {
      override def show(value: A): String =
        val valueTuple = Tuple.fromProductTyped(value)
        val fieldReprs = showTuple[m.MirroredElemTypes, m.MirroredElemLabels](valueTuple)
        fieldReprs.mkString("{", ",", "}")
    }
  }
}
