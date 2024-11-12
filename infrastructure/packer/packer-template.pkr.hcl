# defining variables
variable "project_id" {
  default = "idata2502-ci-cd"
}

# frontend docker image
source "googlecompute" "docker_image" {
  project_id   = var.project_id
  zone         = "europe-north1-a"
  machine_type = "e2-micro"
  ssh_username = "packer"
  image_name   = "docker-{{timestamp}}"
  source_image_family = "debian-11"
}

build {
  sources = ["source.googlecompute.docker_image"]

  provisioner "ansible" {
    playbook_file = "../cac/ansible/main.yml"
  }
}