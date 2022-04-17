import java.util.UUID

plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("kapt") version "1.6.20"
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
    
    implementation("com.google.dagger:dagger:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")
    
    implementation("com.google.code.gson:gson:2.9.0")
    
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "11"
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
