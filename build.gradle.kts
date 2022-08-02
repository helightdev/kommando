import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

val projGroup = "dev.helight.kommando"
val projVersion = "1.3-SNAPSHOT"

group = projGroup
version = projVersion

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
    apply<KotlinPlatformJvmPlugin>()

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        classifier = "sources"
        from(sourceSets.main.get().allSource)
    }

    publishing {
        repositories {
            maven {
                name = "KommandoGithub"
                url = uri("https://maven.pkg.github.com/helightdev/kommando")
                credentials {
                    username = System.getenv("GITHUB_USERNAME")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }

            maven {
                name = "KommandoGitlab"
                url = uri("https://gitlab.minescripts.me/api/v4/projects/20/packages/maven")
                credentials(HttpHeaderCredentials::class) {
                    name = "Private-Token"
                    value = System.getenv("GITLAB_TOKEN")
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }

        }
        publications {
            register("mavenJava", MavenPublication::class) {
                groupId = projGroup
                version = projVersion
                from(components["java"])
                artifact(sourcesJar.get())
            }
        }
    }

    tasks {
        withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
                jvmTarget = "1.8"
            }
        }
    }
}