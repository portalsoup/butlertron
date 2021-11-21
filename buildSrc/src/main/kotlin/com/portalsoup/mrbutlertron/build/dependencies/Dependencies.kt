package com.portalsoup.mrbutlertron.build.dependencies

object Versions {
    const val coroutinesCore = "1.4.3"

    const val exposed = "0.26.2"
    const val hikari = "2.7.8"
    const val flyway = "6.4.0"
    const val kotlinReflect = "1.2.51"
    const val reflections = "0.9.11"
    const val slf4j = "1.7.30"
    const val logback = "1.2.3"
    const val discordJda = "4.2.0_227"
    const val h2 = "1.4.200"
    const val json = "20190722"

    // testing
    const val junit = "4.12"
    const val testng = "7.3.0"

}
object Dependencies {

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib"
    val kotlinScriptRuntime = "org.jetbrains.kotlin:kotlin-script-runtime"
    val kotlinCompilerEmbeddable= "org.jetbrains.kotlin:kotlin-compiler-embeddable"
    val kotlinScriptUtil = "org.jetbrains.kotlin:kotlin-script-util"
    val kotlinScriptCompilerEmbeddable = "org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable"
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"

    val reflections = "org.reflections:reflections:${Versions.reflections}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlinReflect}"

    val discordJda = "net.dv8tion:JDA:${Versions.discordJda}"

    val hikari = "com.zaxxer:HikariCP:${Versions.hikari}"
    val flywayCore = "org.flywaydb:flyway-core:${Versions.flyway}"

    val json = "org.json:json:${Versions.json}"

    val h2 = "com.h2database:h2:${Versions.h2}"
    val exposedCore = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
    val exposedDao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
    val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
    val exposedJavaTime = "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}"


    val slf4jApi = "org.slf4j:slf4j-api:${Versions.slf4j}"
    val logbackCore = "ch.qos.logback:logback-core:${Versions.logback}"
    val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"

    // testing
    val junit = "junit:junit:${Versions.junit}"
    val testng = "org.testng:testng:${Versions.testng}"


}