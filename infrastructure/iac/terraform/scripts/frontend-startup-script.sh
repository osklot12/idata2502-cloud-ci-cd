#!/bin/bash
set -e

# update packages and install docker
apt-get update || { echo "Failed to update packages"; exit 1; }
apt-get install -y docker.io || { echo "Failed to install Docker"; exit 1; }

# enable and start docker
systemctl enable docker || { echo "Failed to enable Docker"; exit 1; }
systemctl start docker || { echo "Failed to start Docker"; exit 1; }

# set user authorization for docker
usermod -aG docker debian || { echo "Failed to add user to Docker group"; exit 1; }

echo "Startup script completed successfully" >> /var/log/startup-script.log