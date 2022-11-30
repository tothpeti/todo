ThisBuild / scalaVersion := "2.13.8"

version := "1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"   % "2.9.0",
  "org.typelevel" %% "cats-effect" % "3.4.1"
)

 lazy val root = (project in file("."))
   .settings(
     name := "todo-backend"
   )
