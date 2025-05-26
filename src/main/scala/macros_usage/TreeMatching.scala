package macros_usage

object TreeMatching {
  def multiply(x: Int, y: Int) = x * y

  demoTreeMatching(multiply(2, 5))
  demoTreeMatching(multiply(1 + 2 + 3 + 4, 5 / 10))
}
