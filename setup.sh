#!/bin/bash
# This script gets invoked by the geni-lib script 'profile.py' to set up a CloudLab node.
# It installs Java, Tomcat, and Maven before building the 'oauth2-w-oidc' project.
# It expects argument 'oidc-server' or 'simple-web-app' to tell which app to deploy.

if [ "$1" != 'oidc-server' ] && [ "$1" != 'simple-web-app' ]; then
  echo "Usage: setup.sh [ oidc-server | simple-web-app ]"
  exit 1
fi

cd /tmp

# Download JDK, Tomcat, and Maven
wget --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u161-b12/2f38c3b165be4555a1fa6e98c45e0808/jdk-8u161-linux-x64.tar.gz
wget http://apache.mirrors.hoobly.com/tomcat/tomcat-8/v8.5.28/bin/apache-tomcat-8.5.28.tar.gz
wget http://mirror.stjschools.org/public/apache/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz

# Create user 'tomcat' with home directory /local/tomcat
sudo mkdir /local/tomcat
sudo groupadd tomcat
sudo useradd -s /bin/false -g tomcat -d /local/tomcat tomcat

# Extract Tomcat, JDK, and Maven
sudo tar -xvzf apache-tomcat-8.5.28.tar.gz -C /local/tomcat/
sudo tar -xvzf jdk-8u161-linux-x64.tar.gz -C /local/
sudo tar -xvzf apache-maven-3.5.2-bin.tar.gz -C /local/

# Setup permissions for tomcat
sudo chgrp -R tomcat /local/tomcat/
sudo chmod -R g+r /local/tomcat/apache-tomcat-8.5.28/conf/
sudo chmod g+x /local/tomcat/apache-tomcat-8.5.28/conf/
sudo chown -R tomcat /local/tomcat/apache-tomcat-8.5.28/webapps/
sudo chown -R tomcat /local/tomcat/apache-tomcat-8.5.28/work/
sudo chown -R tomcat /local/tomcat/apache-tomcat-8.5.28/temp/
sudo chown -R tomcat /local/tomcat/apache-tomcat-8.5.28/logs/
sudo chown -R tomcat /local/tomcat/apache-tomcat-8.5.28/bin/

# Create Tomcat 'setenv.sh' script that sets JAVA_HOME
sudo -H -u tomcat bash -c 'echo "export JAVA_HOME=/local/jdk1.8.0_161" > /local/tomcat/apache-tomcat-8.5.28/bin/setenv.sh'
# Start Tomcat
sudo -H -u tomcat bash -c '/local/tomcat/apache-tomcat-8.5.28/bin/catalina.sh start'

# Set JAVA_HOME and PATH so we can run Maven
export JAVA_HOME=/local/jdk1.8.0_161/
export PATH=/local/apache-maven-3.5.2/bin/:$PATH

# Go to where CloudLab clones the repository
cd /local/repository

# Ugly hack to put the node's hostname in the required places. 
# XXX: Find a proper way of doing this.
sed -i "s/localhost/$HOSTNAME/g" oidc-server/src/main/webapp/WEB-INF/server-config.xml
sed -i "s/localhost/$HOSTNAME/g" simple-web-app/src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml

# Run build
mvn package

# Deploy appropriate .war file
if [ "$1" = 'oidc-server' ]; then
  sudo cp oidc-server/target/oidc-server.war /local/tomcat/apache-tomcat-8.5.28/webapps/
elif [ "$1" = 'simple-web-app' ]; then
  sudo cp simple-web-app/target/simple-web-app.war /local/tomcat/apache-tomcat-8.5.28/webapps/
fi
