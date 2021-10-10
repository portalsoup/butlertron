import com.portalsoup.mrbutlertron.build.dependencies.Dependencies

plugins {
    application
    kotlin("jvm") version "1.3.70"
    id("org.flywaydb.flyway") version "6.4.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

project.group = "com.portalsoup"
project.version = "1.0-SNAPSHOT"

// Variables required from gradle.properties
val discordBotName: String by project
val discordBotToken: String by project
val nookipediaToken: String by project
val ansibleDeployIP: String by project
val deploySshId: String by project
val botGithubUrl: String by project
val doToken: String by project
val commandsLocation: String by project

val pathToAnsibleInventory = "$rootDir/ansible/inventory"

repositories {
    mavenCentral()
    jcenter()
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources")
        }
    }
}

dependencies {
    // kotlin scripting
    implementation(Dependencies.kotlinScriptRuntime)
    implementation(Dependencies.kotlinCompilerEmbeddable)
    implementation(Dependencies.kotlinScriptUtil)
    implementation(Dependencies.kotlinScriptCompilerEmbeddable)

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.kotlinStdlib)

    implementation(Dependencies.reflections)
    implementation(Dependencies.kotlinReflect)

    implementation(Dependencies.discordJda)
    implementation(Dependencies.json)

    implementation(Dependencies.slf4jApi)
    implementation(Dependencies.logbackCore)
    implementation(Dependencies.logbackClassic)

    implementation(Dependencies.h2)
    implementation(Dependencies.exposedCore)
    implementation(Dependencies.exposedDao)
    implementation(Dependencies.exposedJdbc)
    implementation(Dependencies.exposedJavaTime)
    implementation(Dependencies.hikari)
    implementation(Dependencies.flywayCore)

    testImplementation(Dependencies.testng)
}

application {
    mainClassName = "com.portalsoup.mrbutlertron.MainKt"
    applicationDefaultJvmArgs = listOf(
        "-Ddiscord.bot.token=${discordBotToken}",
        "-Ddiscord.bot.name=${discordBotName}",
        "-Dnookipedia.token=${nookipediaToken}",
        "-Dgithub.url=${botGithubUrl}",
        "-Dcommands.location=$commandsLocation"
    )
}

//flyway {
//    url = "jdbc:h2:./database/app"
//    user = "bot"
//    password = ""
//}

// jar stuff

tasks {

    /*
     * Modify existing tasks
     */

    test {
        // enable TestNG support (default is JUnit)
        useTestNG()

        // show standard out and standard error of the test JVM(s) on the console
        testLogging.showStandardStreams = true

        // Fail the 'test' task on the first test failure
        failFast = false
    }

    shadowJar {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf(
                "Main-Class" to application.mainClassName
            ))
        }
    }

    build {
        dependsOn(shadowJar, "buildCommands")
    }

    create<JavaExec>("buildCommands") {
        main = "com.portalsoup.mrbutlertron.v2.CommandHealthcheckKt"
        classpath = sourceSets.main.get().runtimeClasspath
        jvmArgs =  listOf(
            "-Dcommands.location=$commandsLocation"
        )
    }

    /*
     * Create new tasks
     */

    create("deploy") {
        dependsOn("shadowJar", "terraform-apply", "ansible")
    }

    create("terraform-init") {
        onlyIf {
            println("Checking for the presence of terraform/data.tf")
            File("$rootDir/terraform/data.tf").exists()
        }

        doLast {
            project.exec {
                workingDir("$rootDir/terraform")
                commandLine("terraform", "init",
                    "-var", "do_token=$doToken"
                )
            }
        }
    }

    /*
     * By depending on this task, you require a user to manually validate the calculated diff.  If rejected this task
     * prevents downstream tasks from performing any changes
     */
    create("terraform-plan") {
        onlyIf {
            println("Checking for the presence of terraform/data.tf")
            File("$rootDir/terraform/data.tf").exists()
        }

        // configure stdin for prompts
        val run by getting(JavaExec::class) {
            standardInput = System.`in`
        }

        doLast {
            val planResult = project.exec {
                workingDir("$rootDir/terraform")
                commandLine("terraform", "plan",
                    "-var", "ssh_id=${deploySshId}",
                    "-var", "do_token=$doToken"
                )
            }
            when (planResult.exitValue) {
                0 -> {
                    println("Accept this plan? (Type 'yes' to accept)")
                    readLine()
                        ?.takeIf { it == "yes" }
                        ?: throw GradleException("Terraform planned changes were rejected")
                }
                else -> throw GradleException("Unexpected failure $planResult")
            }
        }
    }

    create("terraform-apply") {
        dependsOn("terraform-plan")

        onlyIf {
            println("Checking for the presence of terraform/data.tf")
            File("$rootDir/terraform/data.tf").exists()
        }

        doLast {
            project.exec {
                workingDir("$rootDir/terraform")
                commandLine("terraform", "apply",
                    "-auto-approve",
                    "-var", "ssh_id=${deploySshId}",
                    "-var", "do_token=$doToken"
                )
            }
        }
    }

    create("ansible") {
        mustRunAfter("terraform-plan", "terraform-apply", "shadowJar")

        // Only depend on create-inventory if an inventory file needs to be generated from gradle.properties
        File("$rootDir/ansible/inventory")
            .takeIf { it.exists() }
            ?: dependsOn("create-inventory")

        doLast {
            val sshUser = project.properties["sshUser"]
                ?.let { it as String }
                ?.takeIf { it.isNotEmpty() }
                ?: "root"

            project.exec {
                workingDir("$rootDir/ansible")
                commandLine("ansible-playbook",
                    "-u", sshUser,
                    "--extra-vars", "{\"nookipedia\": ${nookipediaToken}, \"bot\": ${discordBotToken}, \"commandsDir\": $commandsLocation}",
                    "-i", pathToAnsibleInventory,
                    "--flush-cache",
                    "butlertron.yml"
                )
            }
        }
    }

    create("create-inventory") {
        onlyIf {
            println("Checking for the presence of terraform/data.tf")
            !File(pathToAnsibleInventory).exists()
        }

        doLast {
            File(pathToAnsibleInventory).writeText("""
            [butlertron]
            $ansibleDeployIP
        """.trimIndent())
        }
    }
}