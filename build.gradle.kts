group = "dev.helight.kommando"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

plugins {
    id("maven-publish")
}

allprojects {
    repositories {
        maven("https://jitpack.io")
    }
}