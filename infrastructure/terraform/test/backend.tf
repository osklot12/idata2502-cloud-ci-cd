# setting the backend for terraform
terraform {
  backend "gcs" {
    bucket = "idata2502-terraform-backend"
    prefix = "terraform/test/state"
  }
}