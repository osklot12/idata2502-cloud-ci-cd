provider "google" {
  project = "idata2502-ci-cd"
  region  = "europe-north1"
}

# google compute instance for frontend
resource "google_compute_instance" "svelte_frontend" {
  name         = "svelte-frontend"
  machine_type = "e2-micro"
  zone         = "europe-north1-a"

  # configuring boot image
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network       = "default"
    access_config {}
  }

  # using the terraform service account
  service_account {
    email = "terraform-service-account@idata2502-ci-cd.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${file("${path.module}/../.ssh/github_actions_key.pub")}"
  }

  # setting up environment
  metadata_startup_script = <<-EOT
    #!/bin/bash
    apt-get update
    apt-get install -y docker.io
    systemctl enable docker
    systemctl start docker
    usermod -aG docker debian
  EOT
}

# google compute instance for spring
resource "google_compute_instance" "spring_backend" {
  name         = "spring-backend"
  machine_type = "e2-micro"
  zone         = "europe-north1-a"

  # configuring boot image
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network       = "default"
    access_config {}
  }

  # using the terraform service account
  service_account {
    email = "terraform-service-account@idata2502-ci-cd.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${file("${path.module}/../.ssh/github_actions_key.pub")}"  # Public key for the 'debian' user
  }

  # setting up environment
  metadata_startup_script = <<-EOT
    #!/bin/bash
    apt-get update
    apt-get install -y docker.io
    systemctl enable docker
    systemctl start docker
    usermod -aG docker debian
  EOT
}

# google compute instance for postgresql db
resource "google_compute_instance" "postgresql_db" {
  name         = "postgresql-db"
  machine_type = "e2-micro"
  zone         = "europe-north1-a"

  # configuring boot image
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network       = "default"
    access_config {}
  }

  # using the terraform service account
  service_account {
    email = "terraform-service-account@idata2502-ci-cd.iam.gserviceaccount.com"
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${file("${path.module}/../.ssh/github_actions_key.pub")}"  # Public key for the 'debian' user
  }

  # setting up environment
  metadata_startup_script = <<-EOT
    #!/bin/bash
    apt-get update
    apt-get install -y docker.io
    systemctl enable docker
    systemctl start docker
    usermod -aG docker debian
  EOT
}
