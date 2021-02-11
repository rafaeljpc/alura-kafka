repositories {
    jcenter()
    mavenCentral()
}

plugins {
    base
    java
    application
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.noarg") version "1.4.21" apply false
    kotlin("plugin.allopen") version "1.4.21" apply false
}

if (JavaVersion.current() != JavaVersion.VERSION_11) {
    error(
        """
        =======================================================
        RUN WITH JAVA 11
        =======================================================
    """.trimIndent()
    )
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    ignoreFailures = true
    useJUnitPlatform()
}

subprojects {
    if (name == "services" || name == "library") {
        return@subprojects
    } else {
        println("  ${project.name} - ${project.projectDir}")
    }


    buildscript {
        repositories {
            jcenter()
            mavenCentral()
        }
    }

    repositories {
        jcenter()
        mavenCentral()
    }

    tasks.registering(JavaCompile::class) {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
        options.encoding = "UTF-8"
        options.compilerArgs = listOf("-Xlint:unchecked", "-Xlint:deprecation")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            allWarningsAsErrors = true
        }
    }

    when {
        "${project.projectDir}".contains("services") -> {
            apply(plugin = "idea")
            apply(plugin = "java")
            apply(plugin = "kotlin")
            apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
            apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
            apply(plugin = "application")


            application {
                run {
                    val defaultJvmArgs = applicationDefaultJvmArgs.toMutableList()
                    defaultJvmArgs.addAll(
                        listOf(
                            "-DserviceName=${project.name}",
                            property("jvm.max.memory") as String,
                            property("jvm.start.memory") as String
                        )
                    )
                    applicationDefaultJvmArgs = defaultJvmArgs
                }
            }

            tasks.registering(Zip::class) {
                archiveFileName.set(project.name)
            }

            tasks.test {
                ignoreFailures = true
            }

            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.+")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.+")
                implementation("io.github.microutils:kotlin-logging:2.+")
                implementation("ch.qos.logback:logback-classic:1.2.+")

                testImplementation("org.jetbrains.kotlin:kotlin-test")
                testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
                testImplementation("org.junit.jupiter:junit-jupiter:5.+")
                testImplementation("com.ninja-squad:springmockk:2.0.+")
                testImplementation("io.mockk:mockk:1.10.+")
            }
        }
        "${project.projectDir}".contains("library") -> {
            apply(plugin = "idea")
            apply(plugin = "java")
            apply(plugin = "kotlin")
            apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
            apply(plugin = "org.jetbrains.kotlin.plugin.allopen")

            tasks.test {
                ignoreFailures = true
            }

            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.+")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.4.+")

                testImplementation("org.jetbrains.kotlin:kotlin-test")
                testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
                testImplementation("org.junit.jupiter:junit-jupiter:5.+")
            }
        }
        else -> {
        }
    }


}