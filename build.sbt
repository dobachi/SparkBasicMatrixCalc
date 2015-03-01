name := "BasicMatrixCalc"

organization := ""

version := "0.0.1"

scalaVersion := "2.10.4"

resolvers ++= Seq("cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.0.M5b" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-core" % "1.2.1" % "provided" withSources() withJavadoc(),
//  "org.apache.spark" %% "spark-streaming" % "1.2.1" % "provided" withSources() withJavadoc(),
//  "org.apache.spark" %% "spark-sql" % "1.2.1" % "provided" withSources() withJavadoc(),
//  "org.apache.spark" %% "spark-hive" % "1.2.1" % "provided" withSources() withJavadoc(),
  "org.apache.spark" %% "spark-mllib" % "1.2.1" % "provided" withSources() withJavadoc(),
  "org.apache.hadoop" % "hadoop-client" % "2.5.0-cdh5.3.1" % "provided" withJavadoc(),
  "com.github.scopt" %% "scopt" % "3.2.0"
)


initialCommands := "import .basicmatrixcalc._"

