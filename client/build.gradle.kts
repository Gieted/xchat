plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.21"
    application
}

application {
    mainClass.set("pl.pawelkielb.xchat.client.Main")
}

repositories {
}

dependencies {
    implementation(project(":shared"))

    val kotestVersion: String by rootProject
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    val mockkVersion: String by rootProject
    testImplementation("io.mockk:mockk:$mockkVersion")

    val ktorVersion = "2.0.1"
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.slf4j:slf4j-nop:1.7.36")
}

tasks.create("release") {
    dependsOn("installDist")
    group = "build"

    doLast {
        val bin = "build/install/client/bin"
        file("$bin/client").renameTo(file("$bin/xchat"))
        file("$bin/client.bat").renameTo(file("$bin/xchat.bat"))
    }
}
