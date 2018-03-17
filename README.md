# oauth2-w-oidc

This project is the start of a testbed for researching vulnerabilities in OAuth2 w/OIDC.

It is a Maven project with the following modules:

- ```oidc-server```- A simple [overlay](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/Maven-Overlay-Project-How-To) of the [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) server.
- ```simple-web-app```- A copy of MITREid Connect's [simple-web-app](https://github.com/mitreid-connect/simple-web-app), which demonstrates usage of their client library.

## Prerequisites

You will need the following installed on your system:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

## Installing

1. Add entries to your ```/etc/hosts``` file that maps ```oidc-server``` and ```simple-web-app``` to your loopback interface. On my system this looks like:

    ```bash
    127.0.1.1       oidc-server
    127.0.1.1       simple-web-app
    ```

2. Build the project, supplying ```server.host``` for oidc-server and ```client.host``` for simple-web-app:

    ```bash
    mvn clean package -Dserver.host=oidc-server -Dclient.host=simple-web-app
    ```

    If packaging locally is not an option or you run into errors with local packaging, you can use Docker to build the packages:

    ```bash
    docker run --rm -it -v $(pwd):/project mvn clean package -Dserver.host=oidc-server -Dclient.host=simple-web-app 
    ```

    This will build the war files in the docker container and save them to the current directory on the host. The container will be disposed after the build completes.

2. Run:

    ```bash
    docker-compose up
    ```
    or, to run in detached mode:

    ```bash
    docker-compose up -d
    ```
    to list the running containers:
    ```bash
    docker-compose ps
    ```
    or

    ```bash
    docker ps
    ```

The server is accessible at [http://oidc-server/oidc-server/](http://oidc-server/oidc-server/).  
The client app is accessible at [http://simple-web-app/simple-web-app/](http://simple-web-app/simple-web-app/).

The server is set up by default with an in-memory database containing users `user`/`password` and `admin`/`password`.
