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
    applicationDefaultJvmArgs = listOf(
        "-Ddiscord.bot.token=${project.property("discord.bot.token")}",
        "-Ddiscord.bot.name=${project.property("discord.bot.name")}"
    )
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
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")

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

tasks.create("deploy") {
    dependsOn("shadowJar", "terraform-apply", "ansible")
}

tasks.create("terraform-init") {
    onlyIf {
        println("Checking for the presence of terraform/data.tf")
        File("$rootDir/terraform/data.tf").exists()
    }

    doLast {
        project.exec {
            workingDir("$rootDir/terraform")
            commandLine("terraform", "init")
        }
    }
}

/*
 * By depending on this task, you require a user to manually validate the calculated diff.  If rejected this task
 * this task fails prevent downstream tasks from performing any changes
 */
tasks.create("terraform-plan") {
    onlyIf {
        println("Checking for the presence of terraform/data.tf")
        File("$rootDir/terraform/data.tf").exists()
    }

    // configure stdin for prompts
    val run by tasks.getting(JavaExec::class) {
        standardInput = System.`in`
    }

    doLast {
        val planResult = project.exec {
            workingDir("$rootDir/terraform")
            commandLine("terraform", "plan")
        }
        when (planResult.exitValue) {
            0 -> {
                println("Accept this plan? (Type 'yes' to accept)")
                val input = readLine()
                input
                    ?.takeIf { it == "yes" }
                    ?: throw GradleException("Terraform planned changes were rejected")
            }
            else -> throw GradleException("Unexpected failure $planResult")
        }
    }
}

tasks.create("terraform-apply") {
    dependsOn("terraform-plan")

    onlyIf {
        println("Checking for the presence of terraform/data.tf")
        File("$rootDir/terraform/data.tf").exists()
    }

    doLast {
        project.exec {
            workingDir("$rootDir/terraform")
            commandLine("terraform", "apply")
        }
    }
}

tasks.create("ansible") {
    mustRunAfter("terraform-plan", "terraform-apply", "shadowJar")
    onlyIf {
        println("Checking for the presence of ansible/vars.yml")
        File("$rootDir/ansible/vars.yml").exists()
    }

    doLast {
        // optional arg, required if the bot token contained in $vars.yml is vault encrypted
        val vaultFileArg = project.properties["vaultPassword"]
            ?.let { it as String }
            ?.let { File(it) }
            ?.takeIf { it.exists() }
            ?.let { it.absolutePath }
            ?.let { listOf("--vault-password-file", it) }
            ?: listOf()

        val sshUser = project.properties["sshUser"]
            ?.let { it as String }
            ?.takeIf { it.isNotEmpty() }
            ?: "root"

        project.exec {
            workingDir("$rootDir/ansible")
            commandLine("ansible-playbook", "-u", sshUser, "butlertron.yml", *(vaultFileArg.toTypedArray()))
        }
    }
}
