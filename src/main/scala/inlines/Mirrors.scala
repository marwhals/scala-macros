package inlines

import inlines.tools.Show.Show

import scala.compiletime.{constValue, constValueTuple, erasedValue, summonInline}
import scala.deriving.Mirror

// "product"
case class Person(name: String, age: Int, programmer: Boolean)

// "sum"
enum Permissions {
  case READ, WRITE, EXECUTE
}

// automatically derive Show[A] where A can be any Sum type or Product type

object Mirrors {
  // mirror for a product type
  val personMirror = summon[Mirror.Of[Person]] // Mirror.ProductOf[Person]
  // mirror contains all type information

  val daniel: Person = personMirror.fromTuple(("Daniel", 99, true))
  val aTuple: (String, Int, Boolean) = Tuple.fromProductTyped(daniel)

  val className = constValue[personMirror.MirroredLabel] // the name of the class, known at compile time
  val fieldNames = constValueTuple[personMirror.MirroredElemLabels] // names of the fields

  // mirror of sum type
  val permissionMirror = summon[Mirror.Of[Permissions]] // Mirror.SumOf[Permissions]
  // we can get the type name
  // we can list all the cases
  val allCases = constValueTuple[permissionMirror.MirroredElemLabels] // all the cases of the enum as strings, known at compile time

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

  inline def showCC[A <: Product](using m: Mirror.ProductOf[A]): Show[A] = {
    new Show[A] {
      override def show(value: A): String =
        val valueTuple = Tuple.fromProductTyped(value)
        val fieldReprs = showTuple[m.MirroredElemTypes, m.MirroredElemLabels](valueTuple)
        fieldReprs.mkString("{", ",", "}")
    }
  }

  val masterYoda = Person("Master Yoda", 800, false)
  val showPerson = showCC[Person]
  val yodaJson = showPerson.show(masterYoda)

  def main(a: Array[String]) = {
    println(yodaJson)
  }
}


