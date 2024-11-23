# the project
variable "project" {
  default = "idata2502-project"
}

# the service account to use
variable "service_account" {
  default = "git-account@idata2502-project.iam.gserviceaccount.com"
}

# region of servers to use
variable "region" {
  default = "europe-north1"
}

# subnet cidr for the cluster
variable "subnet_cidr" {
  default = "10.0.0.0/24"
}

# machine type for cluster
variable "machine_type" {
  default = "e2-small"
}

# cluster name
variable "cluster_name" {
  default = "tomorrow-cluster-test"
}

# initial node count for cluster
variable "cluster_initial_node_count" {
  default = 1
}

# frontend static ip name
variable "frontend_ip_name_prod" {
  default = "frontend-static-ip-test"
}

# backend static ip name
variable "backend_ip_name_test" {
  default = "backend-static-ip-test"
}

# network name
variable "network_name" {
  default = "tomorrow-network-test"
}

# subnet name
variable "subnet_name" {
  default = "tomorrow-subnet"
}

# pool name
variable "pool_name" {
  default = "test-pool"
}

# node environment label
variable "node_environment_label" {
  default = "test"
}

# the type of disk for nodes
variable "node_disk_type" {
  default = "pd-standard"
}

# the node disk size in gb
variable "node_disk_size" {
  default = 30
}

# the initial node count for node pool
variable "node_pool_initial_count" {
  default = 3
}

# the min node pool count
variable "node_pool_min_node_count" {
  default = 1
}

# the max node pool count
variable "node_pool_max_node_count" {
  default = 5
}

# name for the nat router
variable "nat_router_name" {
  default = "nat-router-prod"
}

# name for the nat router configurations
variable "nat_config_name" {
  default = "nat-config-prod"
}

# master ipv4 cidr block for nat router
variable "nat_config_cidr" {
  default = "10.0.1.0/28"
}