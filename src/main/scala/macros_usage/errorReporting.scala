package macros_usage

import macros.ErrorReporting.{errorReport2, funcWithErrors, funcWithErrorsNoAbort}

object errorReporting {
  val someIntExpression = funcWithErrors(2 + 34) // okay
  // val someIntExpression = funcWithErrors(-1 -3) // does not compile - expr is negative

  // this will not compile because we have a variable
  // val aVariable = 2 + 87
  // val notCompilableExpression = funcWithErrors(aVariable)

  // non-accumulating errors ----- Comment and uncomment to see in SBT
//   val noAccumError = funcWithErrorsNoAbort(-1) // will show only the first error
//   val noAccumError_v2 = funcWithErrorsNoAbort(4) // will show the second error

  // accumulating errors
//   val accumError = errorReport2(-1, 45) // one error for x
//   val accumError_v2 = errorReport2(33, 5) // one error for y
//   val accumError_v3 = errorReport2(-2, 5) // report both errors
}
