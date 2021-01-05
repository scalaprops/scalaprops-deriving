# scalaprops-deriving

[![scaladoc](https://javadoc-badge.appspot.com/com.github.scalaprops/scalaprops-deriving_2.13.svg?label=scaladoc)](https://javadoc-badge.appspot.com/com.github.scalaprops/scalaprops-deriving_2.13/scalaprops/ScalapropsDeriving$.html?javadocio=true)

[scalaz-deriving](https://github.com/scalaz/scalaz-deriving) instances for [scalaprops](https://github.com/scalaprops/scalaprops)

### build.sbt

```scala
libraryDependencies += "com.github.scalaprops" %% "scalaprops-deriving" % "0.2.0"
```

### macro example

```scala
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

// support recursive data structure!
final case class C(y: Boolean, z: Option[A]) extends A
object C {
  implicit val genInstance: Gen[C] =
    Deriving.gen[Gen, C]
  implicit val cogenInstance: Cogen[C] =
    Deriving.gen[Cogen, C]
}
```

### compiler plugin example

#### build.sbt

```scala
addCompilerPlugin("org.scalaz" %% "deriving-plugin" % "3.0.0-M1")
```

#### source code

```scala
import scalaprops.{Cogen, Gen}
import scalaz.annotation.deriving
import scalaprops.ScalapropsDeriving._

@deriving(Gen, Cogen)
sealed trait A extends Any
@deriving(Gen, Cogen)
final case class B(x: Int) extends AnyVal with A
@deriving(Gen, Cogen)
final case class C(y: Boolean, z: Option[A]) extends A
```
