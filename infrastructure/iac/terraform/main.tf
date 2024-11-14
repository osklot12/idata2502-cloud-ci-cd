# Provider, project, and region
provider "google" {
  project = var.project
  region  = var.region
}

# State management backend
terraform {
  backend "gcs" {
    bucket = "tomorrow-terraform-bucket"
    prefix = "terraform/state"
  }
}

# Variable declarations
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

variable "subnet_cidr" {
  description = "CIDR range for the subnet"
  default = "10.0.0.0/24"
}

# Network setup
resource "google_compute_network" "tomorrow_network" {
  name = "tomorrow-app-network"
}

# Subnet setup
resource "google_compute_subnetwork" "tomorrow_subnetwork" {
  name          = "tomorrow-app-subnetwork"
  ip_cidr_range = var.subnet_cidr
  region        = var.region
  network       = google_compute_network.tomorrow_network.id
}

# GKE Cluster Configuration
resource "google_container_cluster" "tomorrow_k8s_cluster" {
  name     = "tomorrow-cluster"
  location = var.region

  # Basic settings for the cluster
  initial_node_count = 3  # Number of nodes in the cluster
  min_master_version  = "1.20"  # Set the version or use default

  # Node pool configuration
  node_config {
    machine_type = var.machine_type
    oauth_scopes = [
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }

  # Enable auto-scaling and set limits
  enable_autoscaling   = true
  min_node_count       = 1
  max_node_count       = 5

  # Network settings
  network    = google_compute_network.tomorrow_network.id
  subnetwork = google_compute_subnetwork.tomorrow_subnetwork.id
}

# Outputting the GKE Cluster endpoint
output "kubernetes_cluster_endpoint" {
  value = google_container_cluster.tomorrow_k8s_cluster.endpoint
}

# Outputting the cluster name for CI/CD configurations
output "kubernetes_cluster_name" {
  value = google_container_cluster.tomorrow_k8s_cluster.name
}
