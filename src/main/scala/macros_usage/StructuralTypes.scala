package macros_usage

import macros.StructuralTypes.Record

/* 
  Structural type = "compile time duck typing"
 */

class Person(val name: String, val age: Int)

object StructuralTypes {
  def makePerson(name: String): Person = new Person(name, 0)
  def makeProgrammer(name: String): Person { val favLanguage: String } = ???
  //                                ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ structural type

  val simpleRecord = Record.make(
    "name" -> "Barry",
    "age" -> 99,
    "favLanguage" -> "Lisp"
  )

  val name = simpleRecord.fields.getOrElse("name", "")
  //Aim: want to use the fields statically
   val nameStatic = simpleRecord.name // property available and with the correct type
  val ageStatic = simpleRecord.age
}