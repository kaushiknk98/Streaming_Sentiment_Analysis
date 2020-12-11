name := "kafka"

version := "0.1"

scalaVersion := "2.11.12"


//val sparkVersion = "2.3.1"
//val sparkVersion = "2.2.2"
val sparkVersion = "2.4.0"


lazy val root = (project in file(".")).
  settings(
    name := "kafka",
    version := "1.0",
    scalaVersion := "2.12.8",
    mainClass in Compile := Some("kafka")
  )

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)
resolvers += "SparkPackages" at "https://dl.bintray.com/spark-packages/maven"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion
)


libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.3.2"
libraryDependencies += "graphframes" % "graphframes" % "0.7.0-spark2.4-s_2.11"
libraryDependencies += "org.apache.kafka" %"kafka-streams" % "2.0.0"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}