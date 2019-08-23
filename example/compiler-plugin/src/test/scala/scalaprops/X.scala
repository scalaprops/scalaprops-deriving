package scalaprops

import scalaz.annotation.deriving
import scalaprops.ScalapropsDeriving._

@deriving(Gen, Cogen)
sealed abstract class X extends Product with Serializable
@deriving(Gen, Cogen)
case object X0 extends X
@deriving(Gen, Cogen)
case class X1(a1: Byte) extends X
@deriving(Gen, Cogen)
case class X2(a1: Byte, a2: Int) extends X
@deriving(Gen, Cogen)
case class X3(a1: Byte, a2: Int, a3: Either[Int, Boolean]) extends X
@deriving(Gen, Cogen)
case class X4(a1: Byte, a2: Int, a3: Either[Int, Boolean], a4: Short) extends X
@deriving(Gen, Cogen)
case class X5(a1: Byte, a2: Int, a3: Either[Int, Boolean], a4: Short, a5: Long) extends X
@deriving(Gen, Cogen)
case class X6(a1: Byte, a2: Int, a3: Either[Int, Boolean], a4: Short, a5: Long, a6: Option[Int]) extends X
@deriving(Gen, Cogen)
case class X7(a1: Byte, a2: Int, a3: Either[Int, Boolean], a4: Short, a5: Long, a6: Option[Int], a7: Boolean) extends X
