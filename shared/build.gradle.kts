plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.21"
}

repositories {
}

dependencies {
    val kotestVersion: String by rootProject
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    val mockkVersion: String by rootProject
    testImplementation("io.mockk:mockk:$mockkVersion")

    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
}
