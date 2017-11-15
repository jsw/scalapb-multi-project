lazy val commonSettings = Seq(
  organization := "github.jsw",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.11", "2.12.4"),
  PB.targets in Compile := Seq(
    scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value
  ),
  libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf",
  libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-json4s" % "0.3.2"
)

lazy val root = (project in file("."))
  .aggregate(shared, service)
  .settings(
    commonSettings,
    Keys.`package` := file(""),
    packageBin in Global := file(""),
    packagedArtifacts := Map(),
    publish := (()),
    publishLocal := (())
  )

lazy val shared = project
  .settings(
    commonSettings,
    name := "shared",
    PB.protoSources in Compile := Seq(file("protos/shared"))
  )

lazy val service = project
  .dependsOn(shared)
  .settings(
    commonSettings,
    name := "service",
    PB.protoSources in Compile := Seq(file("protos")),
    includeFilter in PB.generate := new SimpleFileFilter(
      (f: File) => f.getName.equals("service.proto")
    ),
    libraryDependencies += "github.jsw" %% "shared" % "1.0.0-SNAPSHOT"
  )
