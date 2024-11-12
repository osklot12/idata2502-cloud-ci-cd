packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }

    ansible = {
      version = "~> 1"
      source = "github.com/hashicorp/ansible"
    }
  }
}
