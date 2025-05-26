package macros_usage

import macros.ValueDefs.buildValueDef

import scala.quoted.*

object ValueDefs {
  val scalaLength = buildValueDef
  
  /*  ---- This is what we are trying to achieve using value defs
    synthesized:
    scalaLength = {
      lazy val myValue = "Scala".length // new value definition
      myValue * 4
    }
   */
}
