package wartimzer

import Wartimizer.wartimize

/**
* - Uncomment to see custom scala compile errors from macros
*/
case class Person(name: String, email: String)

case class Programmer(name: String, age: Int, favLanguage: String, gamer: Boolean)

object WartimizerUsage {

  // normal scala code
  val badPractice = "This is Scala: " + Person("John Cena", "jonSeena@wweDaBest.com")

  // Can put this into an SBT plugin
  // This does not compile ---> which is what we want!!!!
//  val linted = wartimize(StringPlusAny)("This is Scala: " + Person("John Cena", "jonSeena@wweDaBest.com"))

  // This does not compile because the variable is not a compile-time known singleton instance (object)
//  val wartimizer: Wartimization = StringPlusAny
//  val linted = wartimize(wartimizer)("This is Scala: " + Person("John Cena", "jonSeena@wweDaBest.com"))

  // validated code - should just compile just fine
  val stringConcatenation = wartimize(StringPlusAny)("Scala is " + "Tasty")

  val firstEven = List(1,2,3,4,5).filter(_ %  2 == 0).headOption
  // rewritten to list.find(_ % 2 == 0)
//   val firstEven_v2 = wartimize(CollectionOptimizer)(List(1,2,3,4,5).filter(_ % 2 == 0).headOption)

  // does not compile because of StringPlusAny - good!  ---- can combo linters and optimizers 🔥
  // val combinedWartimized = wartimize(StringPlusAny, CollectionOptimizer)(List(1,2,3,4,5).filter(_ % 2 == 0).headOption.map("Scala " + _))

  // Map.get.getOrElse
  val simpleMap = Map("Alice" -> 123, "Bob" -> 456, "Charlie" -> 789)
  val personNumber = simpleMap.get("Martin").getOrElse(999)
  val personNumber_v2 = wartimize(CollectionOptimizer)(simpleMap.get("Martin").getOrElse(999))

  // successive map transformation
  val mappedList = List(1,2,3).map(_ + 1).map(_ * 3)
  val optimizedList = wartimize(CollectionOptimizer)(List(1, 2, 3).map(_ + 1).map(_ * 3).map(_.toString + " Scala is great"))

  val combinedCalls = List(1, 2, 3).map(_ + 1).map(_ * 3).map(_.toString + " Scala is great").filter(_.length > 5).headOption
  val optimizedCombinedCalls = wartimize(CollectionOptimizer)(List(1, 2, 3).map(_ + 1).map(_ * 3).map(_.toString + " Scala is great").filter(_.length > 5).headOption)

  val daniel = Programmer("Daniel", 99, "Scala", true)
  val danielCopy = daniel.copy(name="danielciocirlan").copy(age = 102).copy(favLanguage="Scala 3", gamer=true)

  /**
   * - Need to parse the abstract syntax tree explicitly
   */
  /*
  {
  val $2$ =
    {
      val $1$ =
        daniel.copy(
          name = "danielciocirlan",
          daniel.copy$default$2,
          daniel.copy$default$3,
          daniel.copy$default$4)
      $1$.copy(
        $1$.copy$default$1,
        age = 102,
        $1$.copy$default$3,
        $1$.copy$default$4)
    }
  $2$.copy(
    $2$.copy$default$1,
    $2$.copy$default$2,
    favLanguage = "Scala 3",
    gamer = true)
  }

  - target = original object of the copy chain
  - target arguments:
    List(name = "danielciocirlan",
          daniel.copy$default$2,
          daniel.copy$default$3,
          daniel.copy$default$4)
  - chain arguments
    List(
      List(
        $1$.copy$default$1,
        age = 102,
        $1$.copy$default$3,
        $1$.copy$default$4
      ),
      List(
        $2$.copy$default$1,
        $2$.copy$default$2,
        favLanguage = "Scala 3",
        gamer = true
      )
    )
  - target = original object of the copy chain
  - target arguments

  - list arguments

   */

  def main(a: Array[String]) = {
    println(badPractice)
  }

}
