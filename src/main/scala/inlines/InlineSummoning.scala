package inlines

import scala.compiletime.{summonInline, summonFrom}

/**
 * Summoning - Delay the summoning of givens to the call site with summonInline
 * summonFrom - Conditionally inline different expressions based on givens with summonFrom
 */

object InlineSummoning  {

  trait Semigroup[A] {
    def combine(a: A, b: A): A
  }

  def doubleSimple[A](a: A)(using Semigroup[A]): A =
    summon[Semigroup[A]].combine(a, a)

  //   val four = double(2) // doesn't compile / work with type class derivation + inlines


  // Summon inline defers the act of summoninig to the call site of the function and the A type will be concrete to the compiler and it will be clear whether summoning is possible or not.
  inline def double[A](a: A): A = {
    summonInline[Semigroup[A]].combine(a, a)
  }

  given Semigroup[Int] = _ + _
  val four = double(2)

  // --------
  // Conditional summoning
  // --------
  trait Messenger[A] {
    def message: String
  }

  given Messenger[Int] with {
    override def message:String = "some int related message here"
  }

  given Messenger[Double] with {
    override def message: String = "some double related message here"
  }

  /*
    With summonFrom, we can conditionally produce values at compile time (inlined) depending on the givens the compiler finds.
    The pattern match will return the expression for the first matched given found at the call site.
  */

  inline def produceMessage[A] =
    summonFrom {
      case m: Messenger[A] => "Found messenger: " + m.message
      case _ => "Bummer, no messenger found for this type"
    }

  val intMessage = produceMessage[Int] // "Found messenger: " + Messenger[Int].message
  val doubleMessage = produceMessage[Double]
  val floatMessage = produceMessage[Float]
  val otherMessage = produceMessage[String] // "Bummer, no messenger found for this type"
}
