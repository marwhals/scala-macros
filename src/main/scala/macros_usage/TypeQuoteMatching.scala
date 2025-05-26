package macros_usage

import macros.TypeQuoteMatching.matchType
import scala.util.Try

object TypeQuoteMatching {
  val intDescriptor = matchType[Int]


}
