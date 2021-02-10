rootProject.name = "alura-kafka"

file("services").listFiles().filter { it.isDirectory && it.name != "logs" }.forEach {
    include(it.name)
    project(":${it.name}").projectDir = it
}