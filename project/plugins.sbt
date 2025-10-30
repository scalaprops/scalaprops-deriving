addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.6")
addSbtPlugin("com.github.scalaprops" % "sbt-scalaprops" % "0.5.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.12.0")
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.4.0")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.1")

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
)
