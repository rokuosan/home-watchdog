val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val line_version: String by project
val okhttp_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val kaml_version: String by project


plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    kotlin("plugin.serialization") version "1.8.20"
}

group="me.konso"
version="0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean=project.ext.has("development")
    applicationDefaultJvmArgs=listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Ktor Cores
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")

    // Ktor Plugins
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // Postgres
    implementation("org.postgresql:postgresql:$postgres_version")

    // YAML
    implementation("com.charleskorn.kaml:kaml:$kaml_version")

    // LINE
    implementation("com.linecorp.bot:line-bot-model:$line_version")
    implementation("com.linecorp.bot:line-bot-parser:$line_version")
    implementation("com.linecorp.bot:line-bot-api-client:$line_version")
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    // Logger
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}