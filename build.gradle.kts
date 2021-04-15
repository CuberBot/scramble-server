val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val scramble_version: String by project
val xmlgraphics_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "net.lz1998"
version = "0.0.1"
application {
    mainClass.set("net.lz1998.ApplicationKt")
    mainClassName = "net.lz1998.ApplicationKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("org.worldcubeassociation.tnoodle:lib-scrambles:$scramble_version")
    implementation("org.apache.xmlgraphics:batik-transcoder:$xmlgraphics_version")
    implementation("org.apache.xmlgraphics:batik-codec:$xmlgraphics_version")
}
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>{
    archiveFileName.set("${baseName}.${extension}")
}