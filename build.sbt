import sbtrelease._
import ReleaseStateTransformations._

Global / onChangedBuildSource := ReloadOnSourceChanges

def gitHash(): String = sys.process.Process("git rev-parse HEAD").lineStream_!.head

val tagName = Def.setting {
  s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value
  else version.value}"
}
val tagOrHash = Def.setting {
  if (isSnapshot.value) gitHash() else tagName.value
}
val Scala212 = "2.12.12"
val `scalaz-deriving-version` = "2.0.0-M7"

val unusedWarnings = Seq("-Ywarn-unused:imports")

lazy val commonSettings = Def.settings(
  startYear := Some(2019),
  scalapropsCoreSettings,
  unmanagedResources in Compile += (baseDirectory in LocalRootProject).value / "LICENSE.txt",
  scalaVersion := Scala212,
  crossScalaVersions := Seq(Scala212, "2.13.3"),
  organization := "com.github.scalaprops",
  scalacOptions ++= unusedWarnings,
  Seq(Compile, Test).flatMap(c => scalacOptions in (c, console) --= unusedWarnings),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-override",
    "-Xlint:nullary-unit",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
  ),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, v)) if v <= 12 =>
        Seq(
          "-Yno-adapted-args",
          "-Xlint:unsound-match",
          "-Xfuture",
        )
      case _ =>
        Nil
    }
  },
  scalacOptions in (Compile, doc) ++= {
    val tag = tagOrHash.value
    Seq(
      "-sourcepath",
      (baseDirectory in LocalRootProject).value.getAbsolutePath,
      "-doc-source-url",
      s"https://github.com/scalaprops/scalaprops-deriving/tree/${tag}€{FILE_PATH}.scala"
    )
  },
  scalapropsVersion := "0.6.3",
  libraryDependencies ++= Seq(
    "com.github.scalaprops" %% "scalaprops" % scalapropsVersion.value % "test",
  ),
  homepage := Some(url("https://github.com/scalaprops/scalaprops-deriving")),
  licenses := Seq("MIT License" -> url("https://opensource.org/licenses/mit-license")),
  pomExtra := (
    <developers>
      <developer>
        <id>xuwei-k</id>
        <name>Kenji Yoshida</name>
        <url>https://github.com/xuwei-k</url>
      </developer>
    </developers>
    <scm>
      <url>git@github.com:scalaprops/scalaprops-deriving.git</url>
      <connection>scm:git:git@github.com:scalaprops/scalaprops-deriving.git</connection>
      <tag>{tagOrHash.value}</tag>
    </scm>
  ),
  publishTo := sonatypePublishToBundle.value,
  releaseTagName := tagName.value,
  releaseCrossBuild := true,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    UpdateReadme.updateReadmeProcess,
    tagRelease,
    releaseStepCommand("set useSuperShell in ThisBuild := false"),
    ReleaseStep(
      action = { state =>
        val extracted = Project extract state
        extracted.runAggregated(PgpKeys.publishSigned in Global in extracted.get(thisProjectRef), state)
      },
      enableCrossBuild = true
    ),
    releaseStepCommand("set useSuperShell in ThisBuild := true"),
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    UpdateReadme.updateReadmeProcess,
    pushChanges
  )
)

val core = project.settings(
  name := "scalaprops-deriving",
  description := "scalaz-deriving instances for scalaprops",
  commonSettings,
  libraryDependencies ++= Seq(
    "com.github.scalaprops" %% "scalaprops-scalaz" % scalapropsVersion.value,
    "org.scalaz" %% "scalaz-deriving" % `scalaz-deriving-version`,
  )
)

val exampleMacro = project
  .in(file("example/macro"))
  .settings(
    commonSettings,
    skip in publish := true,
    libraryDependencies ++= Seq(
      "org.scalaz" %% "deriving-macro" % `scalaz-deriving-version`,
    )
  )
  .dependsOn(
    core
  )

val exampleCompilerPlugin = project
  .in(file("example/compiler-plugin"))
  .settings(
    commonSettings,
    skip in publish := true,
    libraryDependencies ++= Seq(
      compilerPlugin("org.scalaz" %% "deriving-plugin" % `scalaz-deriving-version` cross CrossVersion.full),
    )
  )
  .dependsOn(
    core
  )

commonSettings
skip in publish := true
