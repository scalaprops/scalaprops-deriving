package scalaprops

import scalaz.annotation.deriving
import scalaprops.ScalapropsDeriving._

@deriving(Gen, Cogen)
sealed trait NoChild
