package type_safe_JDBC

import java.sql.*

object JDBCCommunication {
  def getSchema(query: String): Schema = {
    // load the driver
    Class.forName("org.postgresql.Driver") // necessary to load the driver during macro expansion
    // get a connection to the DB
    val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres")

    // create a prepared statement
    try {
      val statement = connection.prepareStatement(query)
      // get metadata out of that PreparedStatement
      val metadata = statement.getMetaData()

      // => Schema - transform that into a Schema
      Schema.fromMetadata(metadata)
    } finally {
      connection.close() // prevent resource leaks
    }
  }
}
