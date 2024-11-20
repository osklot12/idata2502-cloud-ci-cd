# outputting the reserved ip
output "frontend_static_ip" {
  value = google_compute_address.frontend_static_ip.address
}