rootProject.name = "alura-kafka"


setOf("services", "library")
    .flatMap { file(it).listFiles()!!.toList() }
    .filter { it.isDirectory && it.name != "logs" }
    .forEach {
        include(it.name)
        project(":${it.name}").projectDir = it
    }