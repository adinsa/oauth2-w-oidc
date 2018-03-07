# oauth2-w-oidc

This project is the start of a testbed for researching vulnerabilities in OAuth2 w/OIDC.

It is a Maven project with the following modules:

- ```oidc-server```- A simple [overlay](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/Maven-Overlay-Project-How-To) of the [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) server.
- ```simple-web-app```- A copy of MITREid Connect's [simple-web-app](https://github.com/mitreid-connect/simple-web-app), which demonstrates usage of their client library.

It also serves as a [repository-based CloudLab profile](http://docs.cloudlab.us/creating-profiles.html). The profile is defined in `profile.py`, which invokes `setup.sh` to provision the nodes.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You will need the following installed on your system:

 - [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Maven](https://maven.apache.org/)
 - [Tomcat](https://tomcat.apache.org/)

### Installing

1. Build it:

    ```
    $ mvn package
    ```

2. Copy the `.war` files to Tomcat's `webapps` directory. For example:

    ```
    $ sudo cp oidc-server/target/oidc-server.war /var/lib/tomcat8/webapps/
    ```  
    ```
    $ sudo cp simple-web-app/target/simple-web-app.war /var/lib/tomcat8/webapps/
    ```

The server is accessible at [http://localhost:8080/oidc-server/](http://localhost:8080/oidc-server/).  
The client app is accessible at [http://localhost:8080/simple-web-app/](http://localhost:8080/simple-web-app/).

The server is set up by default with an in-memory database containing users `user`/`password` and `admin`/`password`.

## Deployment

This project can be deployed to CloudLab by creating a [repository-based profile](http://docs.cloudlab.us/creating-profiles.html) using the URL to this repository (https://bitbucket.org/dinsaa/oauth2-w-oidc.git). Once instantiated, you can find the hostnames for each node from the experiment's 'List View' to determine the URLs (e.g. http://aptvm067-1.apt.emulab.net:8080/oidc-server/ and http://aptvm070-1.apt.emulab.net:8080/simple-web-app/).
