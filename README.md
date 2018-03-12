# oauth2-w-oidc

This project is the start of a testbed for researching vulnerabilities in OAuth2 w/OIDC.

It is a Maven project with the following modules:

- ```oidc-server```- A simple [overlay](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/Maven-Overlay-Project-How-To) of the [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) server.
- ```simple-web-app```- A copy of MITREid Connect's [simple-web-app](https://github.com/mitreid-connect/simple-web-app), which demonstrates usage of their client library.

### Prerequisites

You will need the following installed on your system:

 - [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Maven](https://maven.apache.org/)
 - [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

### Installing

1. Build it, supplying ```server.port``` for oidc-server and ```client.port``` for simple-web-app to match those specified in docker-compose.yml: 

    ```
    $ mvn clean package -Dserver.port=8000 -Dclient.port=8001 
    ```
    If packaging locally is not an option or you run into errors with local packaging, you can use Docker to build the packages:

    ```
    $ docker run --rm -it -v $(pwd):/external -w /external maven mvn package
    ```
    This will build the war files in the docker container and save them to the current directory on the host. The container will be disposed after the build completes. 

2. Run:

    ```
    $ docker-compose up
    ```
    or, to run in detached mode:
    
    ```
    $ docker-compose up -d
    ```
    to list the running containers:
    ```
    $ docker-compose ps 
    ```
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;or
    
    ```
    $ docker ps
    ```

The server is accessible at [http://localhost:8000/oidc-server/](http://localhost:8000/oidc-server/).  
The client app is accessible at [http://localhost:8001/simple-web-app/](http://localhost:8001/simple-web-app/).

The server is set up by default with an in-memory database containing users `user`/`password` and `admin`/`password`.
