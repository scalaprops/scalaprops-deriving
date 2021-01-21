addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("com.github.scalaprops" % "sbt-scalaprops" % "0.4.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.1.1")

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
)
