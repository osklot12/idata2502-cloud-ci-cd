#!/bin/bash
set -e

# update packages and install postgresql
apt-get update || { echo "Failed to update packages"; exit 1; }
apt-get install -y postgresql postgresql-contrib || { echo "Failed to install PostgreSQL"; exit 1; }

# configure postgresql to listen on all ips
sed -i "s/^#listen_addresses = 'localhost'/listen_addresses = '*'/" /etc/postgresql/*/main/postgresql.conf

# allow all ips for connection
echo "host all all 0.0.0.0/0 md5" | tee -a /etc/postgresql/*/main/pg_hba.conf

# start and enable postgresql service
systemctl enable postgresql || { echo "Failed to enable PostgreSQL service"; exit 1; }
systemctl start postgresql || { echo "Failed to start PostgreSQL service"; exit 1; }
systemctl restart postgresql || { echo "Failed to restart PostgreSQL service"; exit 1; }

# create database and user
sudo -u postgres psql -c "CREATE DATABASE tomorrow_db;"
sudo -u postgres psql -c "CREATE USER trump WITH ENCRYPTED PASSWORD 'not_harris';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE tomorrow_db TO trump;"

echo "PostgreSQL setup completed successfully" >> /var/log/startup-script.log
