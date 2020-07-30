import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.3.70"
    id("org.flywaydb.flyway") version "6.4.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

project.group = "com.portalsoup"
project.version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.portalsoup.mrbutlertron.MainKt"
    applicationDefaultJvmArgs = listOf("-Dbutlertron.token=${project.property("butlertron.token")}")

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
    implementation("net.dv8tion:JDA:4.0.0_76")
    implementation("org.json:json:20190722")

    compile("com.h2database:h2:1.4.200")

    compile("org.jetbrains.exposed", "exposed-core", "0.23.1")
    compile("org.jetbrains.exposed", "exposed-dao", "0.23.1")
    compile("org.jetbrains.exposed", "exposed-jdbc", "0.23.1")
    compile("org.jetbrains.exposed:exposed-java-time:0.23.1")
    compile("com.zaxxer:HikariCP:2.7.8")

    compile("org.flywaydb:flyway-core:6.4.0")
}

flyway {
    url = "jdbc:h2:./database/app"
    user = "bot"
    password = ""
}

// jar stuff

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf(
                "Main-Class" to application.mainClassName
            ))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}