terraform {
  required_providers {
    digitalocean = {
      source = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
}

provider "digitalocean" {
  token = var.do_token
}
resource "digitalocean_droplet" "bot" {
  image = "ubuntu-18-04-x64"
  name   = "butlertron"
  region = "nyc2"
  size   = "s-1vcpu-1gb"
  ssh_keys = [data.digitalocean_ssh_key.main.id]
}

resource "digitalocean_project" "butlertron" {
  name = "Mr Butlertron"
  resources = [digitalocean_droplet.bot.urn]
}