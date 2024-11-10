# specifying provider, region and project
provider "google" {
  project = "idata2502-ci-cd"
  region  = "europe-north1"
}

# setting backend for state management
terraform {
  backend "gcs" {
    bucket = "tomorrow-terraform-bucket"
    prefix = "terraform/state"
  }
}

# setting variables
variable "machine_type" {
  description = "Machine type for the instances"
  default = "e2-micro"
}

variable "image" {
  description = "Image for the instances"
  default = "debian-cloud/debian-11"
}

variable "region" {
  description = "Region for deployment"
  default = "europe-north1"
}

variable "zone" {
  description = "Zone for deployment"
  default = "europe-north1-a"
}

variable "service_account_email" {
  description = "Email of service account"
  default = "terraform-service-account@idata2502-ci-cd.iam.gserviceaccount.com"
}

variable "service_account_scope" {
  description = "Scope of the service account"
  default = "https://www.googleapis.com/auth/cloud-platform"
}

variable "public_ssh_key_path" {
  description = "Path of the public SSH key for instances"
  default = "../../../.ssh/github_actions_key.pub"
}

variable "subnet_cidr" {
  description = "CIDR range for the subnet"
  default = "10.0.0.0/24"
}

# keeping the retrieval of ssh key content centralized
locals {
  public_ssh_key_content = file("${path.module}/${var.public_ssh_key_path}")
  frontend_startup_script = file("${path.module}/scripts/frontend-startup-script.sh")
  backend_startup_script = file("${path.module}/scripts/backend-startup-script.sh")
  db_startup_script = file("${path.module}/scripts/db-startup-script.sh")
}

# creating a dedicated network for the application
resource "google_compute_network" "tomorrow_network" {
  name = "tomorrow-app-network"
}

# setting up a subnet
resource "google_compute_subnetwork" "tomorrow_subnetwork" {
  name = "tomorrow-app-subnetwork"
  ip_cidr_range = var.subnet_cidr
  region = var.region
  network = google_compute_network.tomorrow_network.id
}

# setting firewall rule for ssh access
resource "google_compute_firewall" "allow_ssh" {
  name = "allow-ssh"
  network = google_compute_network.tomorrow_network.id

  # allowing tcp traffic on port 22
  allow {
    protocol = "tcp"
    ports = ["22"]
  }

  # allowing all ip addresses
  source_ranges = ["0.0.0.0/0"]

  # setting target tags
  target_tags = ["frontend", "backend", "db"]
}

# setting firewall rule for http traffic to the frontend
resource "google_compute_firewall" "allow_http_frontend" {
  name = "allow-http-frontend"
  network = google_compute_network.tomorrow_network.id

  # allowing tcp traffic on port 80
  allow {
    protocol = "tcp"
    ports = ["80"]
  }

  # allowing all ip addresses
  source_ranges = ["0.0.0.0/0"]

  # setting target tags
  target_tags = ["frontend"]
}

# setting firewall rule for http traffic to the backend
resource "google_compute_firewall" "allow_http_backend" {
  name = "allow-http-backend"
  network = google_compute_network.tomorrow_network.id

  # allowing tcp traffic on port 8080
  allow {
    protocol = "tcp"
    ports = ["8080"]
  }

  # allowing all ip addresses
  source_ranges = ["0.0.0.0/0"]

  # setting target tags
  target_tags = ["backend"]
}

# allowing internal db communication
resource "google_compute_firewall" "allow_db_internal" {
  name = "allow-db-internal"
  network = google_compute_network.tomorrow_network.id

  # allowing tcp traffic on port 5432
  allow {
    protocol = "tcp"
    ports = ["5432"]
  }

  # setting source and target tags
  source_tags = ["backend"]
  target_tags = ["db"]
}

# google compute instance for frontend
resource "google_compute_instance" "svelte_frontend" {
  name         = "svelte-frontend"
  machine_type = var.machine_type
  zone         = var.zone

  # configuring boot image
  boot_disk {
    initialize_params {
      image = var.image
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network = google_compute_network.tomorrow_network.id
    subnetwork = google_compute_subnetwork.tomorrow_subnetwork.id
    access_config {}
  }

  # using the terraform service account
  service_account {
    email = var.service_account_email
    scopes = [var.service_account_scope]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${local.public_ssh_key_content}"
  }

  # setting up environment
  metadata_startup_script = local.frontend_startup_script

  # setting frontend tag for network rule purpose
  tags = ["frontend"]

  # adding labels
  labels = {
    environment = "development"
    app = "tomorrow"
  }

  # declaring dependencies
  depends_on = [google_compute_network.tomorrow_network, google_compute_subnetwork.tomorrow_subnetwork]
}

# google compute instance for spring backend
resource "google_compute_instance" "spring_backend" {
  name         = "spring-backend"
  machine_type = var.machine_type
  zone         = var.zone

  # configuring boot image
  boot_disk {
    initialize_params {
      image = var.image
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network = google_compute_network.tomorrow_network.id
    subnetwork = google_compute_subnetwork.tomorrow_subnetwork.id
    access_config {}
  }

  # using the terraform service account
  service_account {
    email = var.service_account_email
    scopes = [var.service_account_scope]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${local.public_ssh_key_content}"
  }

  # setting up environment
  metadata_startup_script = local.backend_startup_script

  # setting backend tag for network rule purpose
  tags = ["backend"]

  # adding labels
  labels = {
    environment = "development"
    app = "tomorrow"
  }

  # declaring dependencies
  depends_on = [google_compute_network.tomorrow_network, google_compute_subnetwork.tomorrow_subnetwork]
}

# google compute instance for postgresql db
resource "google_compute_instance" "postgresql_db" {
  name         = "postgresql-db"
  machine_type = var.machine_type
  zone         = var.zone

  # configuring boot image
  boot_disk {
    initialize_params {
      image = var.image
      size  = 10
    }
  }

  # configuring network interface
  network_interface {
    network = google_compute_network.tomorrow_network.id
    subnetwork = google_compute_subnetwork.tomorrow_subnetwork.id
  }

  # using the terraform service account
  service_account {
    email = var.service_account_email
    scopes = [var.service_account_scope]
  }

  # setting up public ssh key
  metadata = {
    ssh-keys = "debian:${local.public_ssh_key_content}"  # Public key for the 'debian' user
  }

  # setting up environment
  metadata_startup_script = local.db_startup_script

  # setting db tag for network rule purpose
  tags = ["db"]

  # adding labels
  labels = {
    environment = "development"
    app = "tomorrow"
  }

  # declaring dependencies
  depends_on = [google_compute_network.tomorrow_network, google_compute_subnetwork.tomorrow_subnetwork]
}

# outputting the frontend instance ip
output "frontend_instance_ip" {
  value = google_compute_instance.svelte_frontend.network_interface[0].access_config[0].nat_ip
}

# outputting the spring backend instance ip
output "backend_instance_ip" {
  value = google_compute_instance.spring_backend.network_interface[0].access_config[0].nat_ip
}