import java.util.UUID

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

group = "pl.pawelkielb.xchat"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("pl.pawelkielb.xchat.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    val jettyVersion = "11.0.9"
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-webapp:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-slf4j-impl:$jettyVersion")

    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.google.dagger:dagger:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")

    implementation("org.litote.kmongo:kmongo-coroutine:4.5.1")
    implementation("org.litote.kmongo:kmongo-id-serialization:4.5.1")
    implementation("io.projectreactor:reactor-core:3.4.17")

    compileOnly("javax.servlet:javax.servlet-api:4.0.1")

    implementation("org.glassfish.jersey.containers:jersey-container-jetty-servlet:3.0.4")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.0.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.classes {
    doLast {
        file("/build/watch").writeText(UUID.randomUUID().toString())
    }
}

sourceSets {
    create("dev") {
        kotlin {
            val classpath = main.get().compileClasspath
            compileClasspath += classpath
            runtimeClasspath += classpath
        }
    }
}
