name := "myapp"

version := "1.2.3"

scalaVersion := "2.12.3"


// https://mvnrepository.com/artifact/org.scalafx/scalafx
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"

// https://mvnrepository.com/artifact/org.scalafx/scalafxml-core-sfx8 
libraryDependencies += "org.scalafx" %% "scalafxml-core-sfx8" % "0.4" 

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full) 

fork:= true