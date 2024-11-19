# Google provider and region
provider "google" {
  project = var.project
  region  = var.region
}

# Kubernetes provider
provider "kubernetes" {
  host                   = google_container_cluster.tomorrow_k8s_cluster.endpoint
  cluster_ca_certificate = base64decode(google_container_cluster.tomorrow_k8s_cluster.master_auth.0.cluster_ca_certificate)
  token                  = data.google_client_config.default.access_token
}

data "google_client_config" "default" {}

terraform {
  backend "gcs" {
    bucket = "tomorrow-terraform-bucket"
    prefix = "terraform/state"
  }
}

# Variable declarations
variable "project" { default = "idata2502-ci-cd" }
variable "machine_type" { default = "e2-micro" }
variable "region" { default = "europe-north1" }
variable "zone" { default = "europe-north1-a" }
variable "service_account_email" { default = "terraform-service-account@idata2502-ci-cd.iam.gserviceaccount.com" }
variable "subnet_cidr" { default = "10.0.0.0/24" }

# Network setup
resource "google_compute_network" "tomorrow_network" {
  name = "tomorrow-app-network"
}

resource "google_compute_subnetwork" "tomorrow_subnetwork" {
  name          = "tomorrow-app-subnetwork"
  ip_cidr_range = var.subnet_cidr
  region        = var.region
  network       = google_compute_network.tomorrow_network.id
}

# Static IP for ingress
resource "google_compute_address" "ingress_static_ip" {
  name   = "ingress-static-ip"
  region = var.region
}

# GKE Cluster Configuration
resource "google_container_cluster" "tomorrow_k8s_cluster" {
  name     = "tomorrow-cluster"
  location = var.region

  # Master Authentication
  master_auth {
    client_certificate_config {
      issue_client_certificate = false
    }
  }

  # Node Pool Configuration
  remove_default_node_pool = true
  initial_node_count       = 1

  # Default Node Pool (must be created separately)
  node_config {
    machine_type = var.machine_type
    oauth_scopes = [
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }

  # Networking
  network    = google_compute_network.tomorrow_network.id
  subnetwork = google_compute_subnetwork.tomorrow_subnetwork.id
}

# Kubernetes Manifests

# Frontend Deployment and Service
resource "kubernetes_manifest" "frontend_deployment" {
  manifest = yamldecode(file("${path.module}/../../../k8s/frontend/frontend-deployment.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

resource "kubernetes_manifest" "frontend_service" {
  manifest = yamldecode(file("${path.module}/../../../k8s/frontend/frontend-service.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# Backend Deployment and Service
resource "kubernetes_manifest" "backend_deployment" {
  manifest = yamldecode(file("${path.module}/../../../k8s/backend/backend-deployment.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

resource "kubernetes_manifest" "backend_service" {
  manifest = yamldecode(file("${path.module}/../../../k8s/backend/backend-service.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# PostgreSQL Deployment and Service
resource "kubernetes_manifest" "db_deployment" {
  manifest = yamldecode(file("${path.module}/../../../k8s/db/postgres-deployment.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

resource "kubernetes_manifest" "db_service" {
  manifest = yamldecode(file("${path.module}/../../../k8s/db/postgres-service.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# Persistent Volume Claim for PostgreSQL
resource "kubernetes_manifest" "postgres_pvc" {
  manifest = yamldecode(file("${path.module}/../../../k8s/storage/postgres-pvc.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# Secrets for Database Credentials
resource "kubernetes_manifest" "db_credentials" {
  manifest = yamldecode(file("${path.module}/../../../k8s/db/db-credentials.yaml"))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# Ingress
resource "kubernetes_manifest" "ingress" {
  manifest = yamldecode(templatefile("${path.module}/../../../k8s/network/ingress.yaml", {
    static_ip = google_compute_address.ingress_static_ip.address
  }))
  depends_on = [google_container_cluster.tomorrow_k8s_cluster]
}

# Outputs
output "kubernetes_cluster_endpoint" {
  value = google_container_cluster.tomorrow_k8s_cluster.endpoint
}

output "ingress_ip" {
  value = google_compute_address.ingress_static_ip.address
}
