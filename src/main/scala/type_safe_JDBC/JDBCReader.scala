package type_safe_JDBC

trait JDBCReader[A] {
  def read(value: Any): A
}
