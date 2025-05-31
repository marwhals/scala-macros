package type_safe_JDBC

import scala.quoted.*

trait QueryResultDecoder[A] {
  def decode(row: Row): A
}

object QueryResultDecoder {
  transparent inline def make(query: String): QueryResultDecoder[?] =
    ${ makeImpl('query) }


  private def makeImpl(query: Expr[String])(using q: Quotes): Expr[QueryResultDecoder[?]] = {
    // 1 - find the schema TODO (happens at macro expansion time?)
    val schema = JDBCCommunication.getSchema(query.valueOrAbort)

    // 2 - get all column mapping for all column descriptors in the schema
    val descriptorToMappings = schema.values.map(descriptorToMapping)

    // 3 - build the correct type refinement
    val refinedType = makeRefinedType(descriptorToMappings)
    /*
        type RefinedResult = QueryResult {
          val id: idMapping.Result
          val name: nameMapping.Result
          // all other columns
        }
     */

    // 4 - get the column readers
    val columnReaders = getColumnReaders(descriptorToMappings)

    // 5 - get the query decoder
    makeDecoder(columnReaders, refinedType)

  }

  // fetches the correct given ColumnMapping for this column
  private def descriptorToMapping(descriptor: ColumnDescriptor)(using Quotes): DescriptorToMapping = {
    // JDBCType.VL.Varchar => Type[JDBCType.TL.Varchar]
    val jdbcType = toType(descriptor.jdbcType)
    val nullability = toType(descriptor.nullability)
    val colType = toType(descriptor.name)

    (jdbcType, nullability, colType) match {
      case (
        '[type t <: JDBCType.TL; `t`],
        '[type n <: JDBCNullability.TL; `n`],
        '[type c <: String; `c`]
    ) => // fetch a given ColumnMapping[t,n,c]
      val mapping = Expr.summon[ColumnMapping[t,n,c]].getOrElse(ColumnMapping.produceColumnMappingError[t,n,c])
      DescriptorToMapping(descriptor, mapping)
    }
  }

  // create a QueryResult { the correct refinements } based on all column descriptors and mappings
  private def makeRefinedType(descriptorToMapping: List[DescriptorToMapping]): Type[?] = ???

  // fetches all the readers of the correct type so the values can be read correctly into the correct types
  private def getColumnReaders(descriptorToMapping: List[DescriptorToMapping]): Expr[List[JDBCReader[?]]] = ???

  // get the final decoder that can read entire rows into the structural type inferred earlier
  private def makeDecoder(columnReaders: Expr[List[JDBCReader[?]]], refinedType: Type[?]): Expr[QueryResultDecoder[?]] = ???

  private def toType(vlType: JDBCType.VL)(using Quotes): Type[? <: JDBCType.TL] = ???
  private def toType(vlType: JDBCNullability.VL)(using Quotes): Type[? <: JDBCType.TL] = ???
  private def toType(name: String)(using Quotes): Type[? <: String] = ???

  case class DescriptorToMapping(
                                descriptor: ColumnDescriptor,
                                mapping: Expr[ColumnMapping[?, ?, ?]]
                                )



}