dependencies {
    implementation(project(":model"))
    implementation(project(":common-kafka"))
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.apache.kafka:kafka-clients:2.3.+")
    implementation("com.google.code.gson:gson:2+")
}