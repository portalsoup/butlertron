plugins {
    application
    kotlin("jvm") version "1.3.70"
}

project.group = "com.portalsoup"
project.version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.portalsoup.mrbutlertron.MainKt"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.reflections:reflections:0.9.11")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.hibernate:hibernate-gradle-plugin:5.4.1.Final")
    implementation("com.h2database:h2:1.4.198")

    implementation("com.portalsoup:discordbot") {
        version {
            branch = "master"
        }
    }
}