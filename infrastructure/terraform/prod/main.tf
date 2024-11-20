# setting provider
provider "google" {
  project = var.project
  region  = var.region
}

# reserving a static ip for the frontend
resource "google_compute_address" "frontend_static_ip" {
  name   = var.frontend_ip_name_prod
  region = var.region
}

# creating network
resource "google_compute_network" "network" {
  name = var.network_name
}

# creating subnetwork
resource "google_compute_subnetwork" "subnetwork" {
  name          = var.subnet_name
  ip_cidr_range = var.subnet_cidr
  region        = var.region
  network       = google_compute_network.network.id
}

# creating cluster
resource "google_container_cluster" "tomorrow_cluster_prod" {
  name     = var.cluster_name
  location = var.region

  # enable vpc-native (ip aliasing)
  ip_allocation_policy {}

  master_auth {
    client_certificate_config {
      issue_client_certificate = false
    }
  }

  remove_default_node_pool = true
  initial_node_count       = 1

  # configuring network for cluster
  network = google_compute_network.network.id
  subnetwork = google_compute_subnetwork.subnetwork.id

  # disables deletion protection for destroying efficiently
  deletion_protection = false
}

# creating a node pool for the cluster
resource "google_container_node_pool" "default_pool" {
  name     = var.pool_name
  cluster  = google_container_cluster.tomorrow_cluster_prod.name
  location = var.region

  node_config {
    machine_type = var.machine_type

    # setting disk usage configurations
    disk_type = var.node_disk_type
    disk_size_gb = var.node_disk_size

    # disabling external ips for the nodes
    tags = ["no-external-ip"]
    metadata = {
      "disable-legacy-endpoints" = "true"
    }

    # setting label
    labels = {
      environment = var.node_environment_label
    }

    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform",
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring"
    ]
  }

  # autoscaling settings
  autoscaling {
    min_node_count = var.node_pool_min_node_count
    max_node_count = var.node_pool_max_node_count
  }

  # management settings
  management {
    auto_upgrade = true
    auto_repair  = true
  }

  # specify initial node count
  initial_node_count = var.node_pool_initial_count
}

