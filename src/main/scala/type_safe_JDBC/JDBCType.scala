package type_safe_JDBC

object JDBCType {
  // value level
  enum VL {
    case Integer
  }

  // type level
  // supported JDBC types as Scala types to pass as type arguments to ColumnMapping
  sealed trait TL
  object TL {
    sealed trait Integer extends JDBCType.TL
  }

}