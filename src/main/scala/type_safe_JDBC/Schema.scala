package type_safe_JDBC

case class ColumnDescriptor(
                             index: String,
                             name: String,
                             jdbcType: JDBCType.VL,
                             nullable: JDBCNullability.VL
                           )

final case class Schema(values: List[ColumnDescriptor])
