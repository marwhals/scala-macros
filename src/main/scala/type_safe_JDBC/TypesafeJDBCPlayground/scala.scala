package type_safe_JDBC.TypesafeJDBCPlayground

import scala.reflect.Selectable.reflectiveSelectable

object QueryMagic { //types don't need to be named. It is inferred by the compiler
  def run(query: String): List[{val id: Int; val name: String; val age: Int; val hobbies: List[String]}] = ???
}

object TypesafeJDBCPlayground {

  //Ideally want access to all these types when we run this query
  val query = "SELECT * FROM users"
  QueryMagic.run(query).map(_.name)

  def main(a: Array[String]) =
    println("Lets go Habibi")
}