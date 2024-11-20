# the project
variable "project" {
  default = "idata2502-ci-cd"
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
  default = "e2-micro"
}

# cluster name
variable "cluster_name" {
  default = "tomorrow-cluster-prod"
}

variable "frontend_ip_name_prod" {
  default = "frontend-static-ip-prod"
}

# network name
variable "network_name" {
  default = "tomorrow-network-prod"
}

# subnet name
variable "subnet_name" {
  default = "tomorrow-subnet"
}

# pool name
variable "pool_name" {
  default = "prod-pool"
}

# node environment label
variable "node_environment_label" {
  default = "production"
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