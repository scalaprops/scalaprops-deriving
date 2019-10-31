package scalaprops

import scala.collection.mutable

object ScalapropsDerivingTest extends Scalaprops {
  val testGen = Property.forAll { seed: Long =>
    val size = 8
    val set: mutable.Set[Class[_]] = mutable.Set.empty
    Gen[X]
      .infiniteStream(seed = seed)
      .zipWithIndex
      .takeWhile {
        case (x, index) =>
          if (set.size < size && index < 300) {
            set += x.getClass
            true
          } else {
            false
          }
      }
      .force

    set.size == size
  }

  val noChild = Property.forAll {
    try {
      NoChild
      false
    } catch {
      case e: ExceptionInInitializerError =>
        e.getCause.isInstanceOf[IllegalArgumentException]
    }
  }

  val testCogen = Property.forAll { seed: Long =>
    val args = Gen[X].samples(seed = seed, listSize = 1000)
    val function = Gen[X => Int].sample(seed = seed)

    args.map(function).distinct.size > 300
  }
}
