package macros

import scala.quoted.*

/** --- Assistant generated notes 🤖
 * Reflection in the JVM context refers to the ability of a program to examine, analyze, and modify itself at runtime.
 * It allows introspection of the structure and metadata of classes, methods, fields, and other elements of the program,
 * enabling dynamic behavior.
 *
 * Key features of reflection in the JVM:
 * - Inspecting class information: Retrieve details about fields, methods, constructors, and annotations of a class.
 * - Accessing and modifying fields and methods dynamically.
 * - Creating instances of classes at runtime without knowing their names at compile time.
 * - Useful in frameworks, libraries, and tools like dependency injectors, serializers, and testing frameworks.
 *
 * In Scala, reflection is extended through advanced concepts like macros and quoted expressions for compile-time manipulation of code.
 */

object ReflectionBasics {

  // aim is: instance.methodName(arg) --- this is compile time reflection not runtime reflection
  inline def callMethodDynamically[A](instance: A, arg: Int, methodName: String): String =
    ${ callMethodDynamicallyImpl('instance, 'arg, 'methodName) }

  def callMethodDynamicallyImpl[A: Type](instance: Expr[A], arg: Expr[Int], methodName: Expr[String])(using q: Quotes): Expr[String] = {
    import q.reflect.*

    // Term = loosely type Expr = piece of an AST......... AST is built up when a compiler analyses the structure of a program.....
    val term = instance.asTerm // the piece of AST that describes the instance

    // you can inspect Terms --- this is a part of reflection -> see if it has a method or not
    // Select = programmatic construction of a structure e.g. instance.method
    val method = Select.unique(term, methodName.valueOrAbort)

    // an Apply can build a method invocation
    val apply = Apply(method, List(arg.asTerm)) // instance.method(arg) --- programmatic instance of a method call

    // after we're done building the expression, we can turn it into the type we need
    apply.asExprOf[String]
  }

  // generate a tuple with N fields of type A
  // createTuple[3, Int] => (Int, Int, Int)
  transparent inline def createTuple[N <: Int, A](inline value: A) =
    ${ createTupleImpl[N, A]('value) }

  def createTupleImpl[N <: Int : Type, A: Type](value: Expr[A])(using q: Quotes): Expr[Tuple] = {
    import q.reflect.*

    inline def buildTupleSimple(n: Int): Expr[Tuple] =
      if (n == 0) '{ EmptyTuple }
      else '{ $value *: ${ buildTupleSimple(n - 1) } }

    def buildTupleExpr(tpe: Type[? <: AnyKind]): Expr[Tuple] = //🔍
      tpe match {
        case '[A *: rt] => //Recursive case 💡
          '{ $value *: ${ buildTupleExpr(TypeRepr.of[rt].asType) } }
        case _ => '{ EmptyTuple }
      }

    // Usecases - powerful if you want to build your own type signatures and your own type signatures
    inline def buildTupleComplicated(n: Int): Expr[Tuple] = {
      // defn = package for meta-definitions in Scala 💡
      // 1 - build the type constructor => TupleN
      val tupleConstructor = defn.TupleClass(n).typeRef // can use typeRef because .isType returns true
      // 2 - build the type arguments
      val typeArguments = List.fill(n)(TypeRepr.of[A]) // list of all type parameters
      // 3 - build the full type TupleN[A, A, A, ...]
      val fullTupleType = AppliedType(tupleConstructor, typeArguments) // the full type TupleN[A, A, A...] etc
      // 4 - obtain the type descriptor Type[T]
      val actualType = fullTupleType.asType
      // 5 - build the actual tuple in an Expr
      buildTupleExpr(actualType)
    }

    // Type[N] => TypeRepr
    TypeRepr.of[N] match {
      case ConstantType(IntConstant(n)) if n > 1 =>
        buildTupleComplicated(n)
      case _ => report.errorAndAbort(s"I can't build a tuple out of the type ${Type.show[N]}")
    }
  }

}
