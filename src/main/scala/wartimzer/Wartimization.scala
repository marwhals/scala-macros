package wartimzer

import scala.quoted.*

/*
  - Code processor that will transform code of ONE use-case
  - Example (code optimization)
  - before: List(1,2,3).filter(_ % 2 == 0).headOption
  - after: List(1,2,3).find(_ % 2 == 0)

  Example (wart remover)
  before: "Scala is " + Person("Martin Odersky", "martin@gmail.com")
  after: (should not compile)

*/

trait Wartimization {
  self: Singleton => // all Wartimization instances must be objects, so that they can be referred to as constants
  def treeMap(using q: Quotes): q.reflect.TreeMap
}

