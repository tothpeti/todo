ThisBuild / scalaVersion := "3.2.1"

libraryDependencies ++= Seq(
  // Core
  "org.typelevel"         %% "cats-core"              % "2.9.0",
  "org.typelevel"         %% "cats-effect"            % "3.4.1",
  "org.http4s"            %% "http4s-dsl"             % "0.23.16",
  "org.http4s"            %% "http4s-circe"           % "0.23.16",
  "org.http4s"            %% "http4s-ember-server"    % "0.23.16",
  "com.github.pureconfig" %% "pureconfig-core"        % "0.17.2",
  "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.2",
  // Database
  "org.tpolecat" %% "doobie-core"   % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
  "org.flywaydb"  % "flyway-core"   % "9.10.2",
  // Logging
  "org.typelevel" %% "log4cats-slf4j"  % "2.5.0",
  "ch.qos.logback" % "logback-classic" % "1.4.5"
)

lazy val root = (project in file("."))
  .settings(
    name := "todo"
  )
