package type_safe_JDBC

import scala.reflect.Selectable.reflectiveSelectable

object QueryMagic { //types don't need to be named. It is inferred by the compiler
  def run[A](query: String): List[A] = ???
}

object Ideal {
  //Ideally want access to all these types when we run this query. Analyse it at compile time
  val query = "SELECT * FROM users"
  //  QueryMagic.run(query).map(_.name)

  // 1 - find the schema
  val schema = JDBCCommunication.getSchema(query)

  // 2 - identify column mappings for all columns (synthesized as givens) - will fail if there is no given
  val idMapping: ColumnMapping[JDBCType.TL.Integer, JDBCNullability.TL.Nullable, "id"] = ??? //Figures out the correct types

  // 3 - infer the result type
  type RefinedResult = QueryResult {
    val id: idMapping.Result // same as Int
    // same for the rest of the columns (automatically)
  }

  // 4 - ability to read value from JDBC into the correct types
  val idColumnReader = idMapping.reader
  // same for the rest of the columns (automatically)

  // 5 - run the query and return the correct type
  val magicResult = QueryMagic.run[RefinedResult](query)
  //                              ^^^^^^^^^^^^^^ Passed by the macro automatically

  // 6 - profit
  val ids = magicResult.map(_.id)
}

object TypesafeJDBCPlayground {

  def getValues[T <: JDBCType.TL, N <: JDBCNullability.TL, C <: String](rows: List[Row], colName: String)(using mapper: ColumnMapping[T, N, C]): List[mapper.Result] = {
    rows
      .map(_.values)
      .map(_.apply(colName))
      .map(v => mapper.reader.read(v))
  }

  def main(a: Array[String]) = {
    val rows = JDBCCommunication.runQuery("SELECT * FROM users")
    rows.foreach(println)
    val names = getValues[JDBCType.TL.Varchar, JDBCNullability.TL.NonNullable, "name"](rows, "name")
    names.foreach(println)
    val hobbies = getValues[JDBCType.TL.Array[JDBCType.TL.Varchar], JDBCNullability.TL.NonNullable, "hobbies"](rows, "hobbies")
    hobbies.foreach(println)
  }
}