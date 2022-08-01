import org.jetbrains.dokka.gradle.DokkaPlugin

group = "dev.helight.kommando"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10"
    `maven-publish`
}


subprojects {
    apply<MavenPublishPlugin>()
    apply<DokkaPlugin>()

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    tasks {
        withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf("-Xexplicit-api=strict", "-opt-in=kotlin.RequiresOptIn")
                jvmTarget = "1.8"
            }
        }
    }
}