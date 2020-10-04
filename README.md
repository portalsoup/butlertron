# Local run

Create a gradle.properties file and set the property `discord.bot.token`.  Then run:

    ./gradlew run
    
    
# Deployment:

Deployment happens in 3 parts

* produce artifact
* Create infrastructure
* Provision infrastructure

Gradle's shadowjar plugin produces an artifact.
Terraform creates remote infrastructure
Ansible provisions, copies and starts the artifact

Before running the gradle task to perform a deployment, see the ansible and terraform sections below and create the 
required secrets files.
    
## ./gradlew deploy
Perform a deployment.  This is the entrypoint and will build and package the full project.  Create/update infrastructure
and finally provision that infrastructure.

## Optional arguments:
* `PvaultPasswordFile=path/to/file/containing/password`
A file containing the ansible vault password with no padding
* `-PsshUser=username`
The username ansible should ssh with

## Ansible
The only requirements for a remote server is that it must be an debian flavor running the apt tool, and the ssh user 
have privileged access.  This is meant to run on cattle servers that can be created/destroyed at will.

Required files:

### ansible/vars.yml
This file may be encrypted with the ansible vault. When encrypted, this file can be added to a private repo.

If encrypted, then the gradle arg -PvaultPasswordFile is required.

Contains one line (when decrypted):

    bot.token=insert-discord-bot-token-here
    
    
### ansible/inventory
This file contains the IP address of the server that should be provisioned to run this bot. 
Contains these lines:
   
    [butlertron]
    server-ip-address-goes-here
    
## Terraform

Required files:

### terraform/data.tf

Contains this resource:

    data "digitalocean_ssh_key" "main" {
      name = "digitalocean-ssh-key-name"
    }
    