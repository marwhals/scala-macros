package inlines


import inlines.tools.Show
import inlines.tools.Show.derived

import scala.compiletime.{constValue, constValueTuple, erasedValue, summonInline}
import scala.deriving.Mirror

// "product"
/*
- Compiler will look for a method "derived" in the Show object
- Such that it returns a Show[Person]
- 'derives show' will give us a GIVEN Show[Person]
*/
case class Person(name: String, age: Int, programmer: Boolean) derives Show

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

  val masterYoda = Person("Master Yoda", 800, false)
  val showPerson = Show.derived[Person]
  val showPerson_v2 = summon[Show[Person]] // implicit
  val showPerson_v3 = Person.derived$Show //explicit type class instance, synthesised by the compiler
  val yodaJson = showPerson.show(masterYoda)

  def printThing[A](thing: A)(using Show[A]) =
    println(summon[Show[A]].show(thing))

  def main(a: Array[String]) = {
    println(yodaJson) // <--- Show[Person] passed implicitly here
  }
}
/**
Type Class Derivation
 - Allow the compiler to auto-synthesize type class instances as given values
 - Requirement: a public ''derived'' method in the companion object
  - no proper argument list
  - Show[that type] as return value
 - Signature and implementation are flexible
  - Can be inline
  - Can be used generics (and it usually will)
  - Can have using clauses
  - Can be implemented in any way
*/

