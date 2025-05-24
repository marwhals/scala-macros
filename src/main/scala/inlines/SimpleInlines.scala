package inlines

import scala.util.Random

object SimpleInlines {

  def increment(x: Int): Int = x + 1
  inline def inc(x: Int): Int = x + 1

  /**
  - Method invocations are replaced with the body of the method
   */
  val aNumber: Int = 3
  val four: Int = inc(aNumber) // reduces to aNumber + 1 at COMPILE TIME

  val eight = inc(2 * aNumber + 1)
  /*
    reduces to
    {
      val proxy = 2 * aNumber + 1
      proxy + 1
    }
   */

  // inline arguments - expanded within the method body
  inline def incia(inline x: Int) = x + 1
  // Inline arguments are replaced with the expression they're used with
  val eight_v2 = incia(2 * aNumber + 1) // reduces to 2 * aNumber + 1 + 1
  // conceptually similar to by-name invocation, only that the args are expanded at COMPILE TIME

  // transparent inline
  // lets the compiler see the most concrete type of the implementation
  // Careful with "transparent" on big/recursive types as this can make the copmiler slow
  transparent inline def wrap(x: Int): Option[Int] = Some(x)

  val anOption: Option[Int] = wrap(7)
  val aSome: Some[Int] = wrap(7)

  // perf optimisation
  def testInline() = {
    inline def loop[A](inline start: A, inline condition: A => Boolean, inline advance: A => A)(inline action: A => Any) = {
      var a = start
      while (condition(a)) {
        action(a)
        a = advance(a)
      }
    }

    val start = System.currentTimeMillis()
    val r = Random().nextInt(10000)
    val u = Random().nextInt(10000)
    val arr = Array.ofDim[Int](10000)
    loop(0, _ < 10000, _ + 1) { i =>
      loop(0, _ < 100000, _ + 1) { j =>
        arr(i) = arr(i) + u
      }
      arr(i) = arr(i) + r
    }

    println(s"Inline version: ${(System.currentTimeMillis() - start) / 1000.0} s")
  }

  def testNoInline() = {
    def loop[A](start: A, condition: A => Boolean, advance: A => A)(action: A => Any) = {
      var a = start
      while (condition(a)) {
        action(a)
        a = advance(a)
      }
    }

    val start = System.currentTimeMillis()
    val r = Random().nextInt(10000)
    val u = Random().nextInt(10000)
    val arr = Array.ofDim[Int](10000)
    loop(0, _ < 10000, _ + 1) { i =>
      loop(0, _ < 100000, _ + 1) { j =>
        arr(i) = arr(i) + u
      }
      arr(i) = arr(i) + r
    }

    println(s"No inline version: ${(System.currentTimeMillis() - start) / 1000.0} s")
  }

  def main(args: Array[String]) = {
    testInline()
    testNoInline()
  }

}
