# setting the backend for terraform
terraform {
  backend "gcs" {
    bucket = "tomorrow-terraform-bucket"
    prefix = "terraform/prod/state"
  }
}