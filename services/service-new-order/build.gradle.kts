dependencies {
    implementation(project(":model"))
    implementation(project(":common-kafka"))
    implementation("org.apache.kafka:kafka-clients:2.8.+")
    implementation("com.google.code.gson:gson:2.8.+")
}