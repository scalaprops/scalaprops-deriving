package example

import scalaprops.{Cogen, Gen}
import scalaz.Deriving
import scalaprops.ScalapropsDeriving._

sealed trait A extends Any
object A {
  implicit val genInstance: Gen[A] =
    Deriving.gen[Gen, A]
  implicit val cogenInstance: Cogen[A] =
    Deriving.gen[Cogen, A]
}

final case class B(x: Int) extends AnyVal with A
object B {
  implicit val genInstance: Gen[B] =
    Deriving.gen[Gen, B]
  implicit val cogenInstance: Cogen[B] =
    Deriving.gen[Cogen, B]
}

final case class C(y: Boolean, z: Option[A]) extends A
object C {
  implicit val genInstance: Gen[C] =
    Deriving.gen[Gen, C]
  implicit val cogenInstance: Cogen[C] =
    Deriving.gen[Cogen, C]
}
