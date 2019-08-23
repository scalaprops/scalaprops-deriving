package scalaprops

import scalaprops.ScalapropsScalaz._
import scalaz.iotaz.Prod
import scalaz.{Applicative, Deriving, InvariantApplicativez, Name}
import scalaz.std.list._

object ScalapropsDeriving {

  implicit val derivingGen: Deriving[Gen] = new InvariantApplicativez[Gen] with Deriving[Gen] {

    override def xcoproductz[Z, A <: TList, FA <: TList](
      tcs: Prod[FA]
    )(f: Cop[A] => Z, g: Z => Cop[A])(implicit ev: PairedWith[A, FA]): Gen[Z] = {
      if (tcs.values.isEmpty) {
        throw new IllegalArgumentException("Can not create Gen. for sealed class/trait without child classes")
      } else {
        Gen.gen[Z] { (size, rand0) =>
          val (rand1, n) = rand0.nextInt
          val index = {
            val x = n % tcs.values.length
            if (x < 0) -x else x
          }
          val (rand2, x) = tcs.values(index).asInstanceOf[Name[Gen[Z]]].value.f(size, rand1)
          (rand2, g(x).value.asInstanceOf[Z])
        }
      }
    }

    override def xproductz[Z, A <: TList, FA <: TList](
      tcs: Prod[FA]
    )(f: Prod[A] => Z, g: Z => Prod[A])(implicit ev: PairedWith[A, FA]): Gen[Z] = {
      tcs.values.asInstanceOf[Seq[Name[Gen[Any]]]] match {
        case Seq() =>
          Gen.value(
            f(Prod.unsafeApply(Nil))
          )
        case Seq(a1) =>
          for {
            x1 <- a1.value
          } yield f(Prod.unsafeApply(x1 :: Nil))
        case Seq(a1, a2) =>
          for {
            x1 <- a1.value
            x2 <- a2.value
          } yield f(Prod.unsafeApply(x1 :: x2 :: Nil))
        case Seq(a1, a2, a3) =>
          for {
            x1 <- a1.value
            x2 <- a2.value
            x3 <- a3.value
          } yield f(Prod.unsafeApply(x1 :: x2 :: x3 :: Nil))
        case xs :+ x =>
          Applicative[Gen]
            .sequence(
              xs.foldRight(x.value :: Nil)(_.value :: _)
            )
            .map { z =>
              f(Prod.unsafeApply(z))
            }
      }
    }
  }

  implicit val derivingCogen: Deriving[Cogen] = new InvariantApplicativez[Cogen] with Deriving[Cogen] {
    override def xproductz[Z, A <: TList, FA <: TList](
      tcs: Prod[FA]
    )(f: Prod[A] => Z, g: Z => Prod[A])(implicit ev: PairedWith[A, FA]): Cogen[Z] = {
      val values = tcs.values.asInstanceOf[Seq[Name[Cogen[Any]]]]
      values match {
        case Seq() =>
          Cogen.conquer[Z]
        case Seq(a1) =>
          new Cogen[Z] {
            override def cogen[B](a: Z, state: CogenState[B]): CogenState[B] = {
              val Seq(x1) = g(a).values
              a1.value.cogen(
                x1,
                state
              )
            }
          }
        case Seq(a1, a2) =>
          new Cogen[Z] {
            override def cogen[B](a: Z, state: CogenState[B]): CogenState[B] = {
              val Seq(x1, x2) = g(a).values
              a1.value.cogen(
                x1,
                a2.value.cogen(
                  x2,
                  state
                )
              )
            }
          }
        case Seq(a1, a2, a3) =>
          new Cogen[Z] {
            override def cogen[B](a: Z, state: CogenState[B]): CogenState[B] = {
              val Seq(x1, x2, x3) = g(a).values
              a1.value.cogen(
                x1,
                a2.value.cogen(
                  x2,
                  a3.value.cogen(x3, state)
                )
              )
            }
          }
        case _ =>
          new Cogen[Z] {
            override def cogen[B](a: Z, state: CogenState[B]): CogenState[B] = {
              values.zip(g(a).values).foldRight(state) {
                case ((cogen, value), s) =>
                  cogen.value.cogen(value, s)
              }
            }
          }
      }
    }

    override def xcoproductz[Z, A <: TList, FA <: TList](
      tcs: Prod[FA]
    )(f: Cop[A] => Z, g: Z => Cop[A])(implicit ev: PairedWith[A, FA]): Cogen[Z] = {
      if (tcs.values.isEmpty) {
        Cogen.conquer[Z]
      } else {
        new Cogen[Z] {
          override def cogen[B](a: Z, state: CogenState[B]): CogenState[B] = {
            val cop = g(a)
            val z = cop.value.asInstanceOf[Z]
            tcs.values(cop.index).asInstanceOf[Name[Cogen[Any]]].value.cogen(z, state)
          }
        }
      }
    }
  }
}
