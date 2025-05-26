package macros_usage

import macros.BuildingExpressions.{createDefaultPermissions, describePermissions}

object BuildingExpressions {
  val defaultPermissions = createDefaultPermissions()

  // convert exprs to regular data structures example/usage
  val description = describePermissions(Permissions.Custom(List("photos", "code")))
  
}
