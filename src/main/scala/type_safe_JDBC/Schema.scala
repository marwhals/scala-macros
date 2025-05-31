package type_safe_JDBC

import java.sql.{ResultSetMetaData, Types}

case class ColumnDescriptor(
                             index: Int,
                             name: String,
                             jdbcType: JDBCType.VL,
                             nullable: JDBCNullability.VL
                           )

final case class Schema(values: List[ColumnDescriptor])

object Schema {
  def fromMetadata(metadata: ResultSetMetaData): Schema = {
    val descriptor = for {
      i <- 1 to metadata.getColumnCount()
    } yield ColumnDescriptor(
      index = i,
      name = getName(metadata, i),
      jdbcType = getType(metadata, i),
      nullable = getNullable(metadata, i)
    )

    Schema(descriptor.toList)
  }


  private def getName(metadata: ResultSetMetaData, i: Int): String =
    metadata.getColumnLabel(i)

  private def getType(metadata: ResultSetMetaData, i: Int): JDBCType.VL =
    metadata.getColumnType(i) match {
      case Types.VARCHAR => JDBCType.VL.Varchar
      case Types.INTEGER => JDBCType.VL.Integer
      case Types.DOUBLE => JDBCType.VL.Double
      case Types.BOOLEAN => JDBCType.VL.Boolean
      case Types.ARRAY if metadata.getColumnTypeName(i).contains("character") => JDBCType.VL.Varchar
        // FIXME: add all other cases if you want this library production-ready
      case _ => JDBCType.VL.NotSupported
    }

  private def getNullable(metadata: ResultSetMetaData, i: Int): JDBCNullability.VL =
    metadata.isNullable(i) match {
      case ResultSetMetaData.columnNoNulls => JDBCNullability.VL.NonNullable
      case _ => JDBCNullability.VL.Nullable // assuming nullable in all other cases
    }

}
