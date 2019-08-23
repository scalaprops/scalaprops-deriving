package example

import scalaprops.{Cogen, Gen}
import scalaz.annotation.deriving
import scalaprops.ScalapropsDeriving._

@deriving(Gen, Cogen)
sealed trait A extends Any
@deriving(Gen, Cogen)
final case class B(x: Int) extends AnyVal with A
@deriving(Gen, Cogen)
final case class C(y: Boolean, z: Option[A]) extends A
