# defining variables
variable "project_id" {
  description = "The project ID"
  default = "idata2502-ci-cd"
}

variable "zone" {
  description = "The zone"
  default = "europe-north1-a"
}

variable "machine_type" {
  description = "The type of machine"
  default = "e2-micro"
}

variable "source_image_family" {
  description = "The source image family"
  default = "debian-11"
}

variable "image_family" {
  description = "The image family"
  default = "tomorrow-standard-family"
}

variable "ssh_username" {
  description = "The username for the SSH connection"
  default = "debian"
}

# defining the image to build
source "googlecompute" "app_image" {
  project_id   = var.project_id
  zone         = var.zone
  machine_type = var.machine_type
  ssh_username = var.ssh_username
  image_name   = "docker-{{timestamp}}"
  source_image_family = var.source_image_family
  image_family = var.image_family
}

build {
  sources = ["source.googlecompute.app_image"]

  provisioner "ansible" {
    playbook_file = "../cac/ansible/main.yml"
  }
}