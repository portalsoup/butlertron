# Local run

Create a gradle.properties file and set the property `discord.bot.token`.  Then run:

    ./gradlew run
    
    
# Deployment:

## ./gradlew deploy
Perform a deployment.  This is the entrypoint and will build and package the full project.  Create/update infrastructure
and finally provision that infrastructure.


Deployment happens in 3 parts

* produce artifact using gradle shadowJar plugin
* Create infrastructure using terraform
* Provision infrastructure using ansible

Before running the gradle task to perform a deployment, ensure that all the follow variables are set in gradle.

## Required gradle.properties

    discordBotToken={value}
    discordBotName={value}
    nookipediaToken={value}
    ansibleDeployIP={value}
    deploySshId={value}

## Ansible
The only requirements for a remote server is that it must be an debian flavor running the apt tool, and the ssh user 
have privileged access.  This is meant to run on cattle servers that can be created/destroyed at will.
    
## Terraform

The plan is for digitalocean, and the configured digital ocean ssh key id is required.

# Commands

Commands are logical groupings of jobs, which are independent behaviors the bot can act against and respond using.


## Adding commands

Commands are dynamically loaded on app launch from the resources directory from kotlin script files.  
The script must return a `Command` value.  An example `helloworld.kts` command using th DSL api can look like this:

    import com.portalsoup.mrbutlertron.v2.core.reply
    import com.portalsoup.mrbutlertron.v2.dsl.command

    command {
        name = "Simple hello world greeting"
        description = "Basic call and answer command"

        job {
            precondition { it.formattedMessage().startsWith("!hello") }
            action { it.reply("world!") }
        }
    }

A command can have multiple independent jobs:

    import com.portalsoup.mrbutlertron.v2.core.reply
    import com.portalsoup.mrbutlertron.v2.dsl.command

    command {
        name = "Favorite things"
        description = "Some of my favorite things"

        job {
            precondition { it.formattedMessage().startsWith("!color") }
            action { it.reply("Green") }
        }

        job {
            precondition { it.formattedMessage().startsWith("!fruit") }
            action { it.reply("Apple") }
        }
    }