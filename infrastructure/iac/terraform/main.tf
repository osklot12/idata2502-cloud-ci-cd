# specifying provider, region and project
provider "google" {
  project = var.project
  region  = var.region
}

# setting backend for state management
terraform {
  backend "gcs" {
    bucket = "tomorrow-terraform-bucket"
    prefix = "terraform/state"
  }
}

# setting variables
variable "project" {
  description = "The project ID"
  default = "idata2502-ci-cd"
}

variable "machine_type" {
  description = "Machine type for the instances"
  default = "e2-micro"
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

variable "subnet_cidr" {
  description = "CIDR range for the subnet"
  default = "10.0.0.0/24"
}

variable "public_ssh_key_content" {
  description = "Public SSH key content"
  default = ""
}

data "google_compute_image" "latest_image" {
  family = "tomorrow-standard-family"
  project = var.project
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
  target_tags = ["tomorrow-server"]
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
  target_tags = ["tomorrow-server"]
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
  target_tags = ["tomorrow-server"]
}

# google compute instance
resource "google_compute_instance" "tomorrow_server" {
  name         = "tomorrow-server"
  machine_type = var.machine_type
  zone         = var.zone

  # configuring boot image
  boot_disk {
    initialize_params {
      image = data.google_compute_image.latest_image.self_link
      size  = 30
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
    ssh-keys = "debian:${var.public_ssh_key_content}"
  }

  # setting frontend tag for network rule purpose
  tags = ["tomorrow-server"]

  # adding labels
  labels = {
    environment = "development"
    app = "tomorrow"
  }

  # declaring dependencies
  depends_on = [google_compute_network.tomorrow_network, google_compute_subnetwork.tomorrow_subnetwork]
}

# outputting the isntance ip
output "tomorrow_server_instance" {
  value = google_compute_instance.tomorrow_server.network_interface[0].access_config[0].nat_ip
}