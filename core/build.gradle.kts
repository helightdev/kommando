import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

val kordVersion = "0.8.0-M15"

application {
    mainClass.set("com.example.ApplicationKt")
}

dependencies {
    api("com.google.inject:guice:5.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")
    implementation("com.google.code.gson:gson:2.9.0")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
