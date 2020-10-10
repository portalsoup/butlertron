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