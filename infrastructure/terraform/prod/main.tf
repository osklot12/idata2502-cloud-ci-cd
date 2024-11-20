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
  initial_node_count = var.cluster_initial_node_count

  # enable vpc-native (ip aliasing)
  ip_allocation_policy {}

  # configuring network for cluster
  network = google_compute_network.network.id
  subnetwork = google_compute_subnetwork.subnetwork.id


  private_cluster_config {
    enable_private_nodes = true
    # making master api public for convenient access: should not be used in real scenarios
    enable_private_endpoint = false
  }

  master_auth {
    client_certificate_config {
      issue_client_certificate = false
    }
  }

  remove_default_node_pool = true

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

# setting up a nat router
resource "google_compute_router" "nat_router" {
  name    = var.nat_router_name
  network = google_compute_network.network.id
  region  = var.region
}

# configuration for the nat router
resource "google_compute_router_nat" "nat_config" {
  name                               = var.nat_config_name
  router                             = google_compute_router.nat_router.name
  region                             = var.region
  nat_ip_allocate_option             = "AUTO_ONLY" # Auto-allocate IP
  source_subnetwork_ip_ranges_to_nat = "ALL_SUBNETWORKS_ALL_IP_RANGES"
}
