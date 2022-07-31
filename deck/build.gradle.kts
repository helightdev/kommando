import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val deckVersion = "0.5.1-BOT"

application {
    mainClass.set("com.example.ApplicationKt")
}

dependencies {
    implementation(project(":core"))
    implementation("com.github.SrGaabriel.deck:deck-core:$deckVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.9.0")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
