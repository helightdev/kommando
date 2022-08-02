plugins {
    kotlin("jvm")
}

val deckVersion = "0.5.2-BOT"


dependencies {
    implementation(project(":core"))
    implementation("com.github.SrGaabriel.deck:deck-core:$deckVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("io.ktor:ktor-client:2.0.2")
}
