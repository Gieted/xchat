plugins {
    kotlin("jvm")
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
