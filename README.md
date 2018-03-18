# oauth2-w-oidc

This project is an effort to replicate the results of Mainka et al. [1] on second-order vulnerabilities in OpenID Connect.

It is a Maven project with the following modules:

- ```honest-op```- The honest OpenID Provider (OP).
- ```malicious-op```- The malicious OpenID Provider (OP). 
- ```honest-client```- A client based off of MITREid Connect's [simple-web-app](https://github.com/mitreid-connect/simple-web-app).

Both ```honest-op``` and ```malicious-op``` are [overlays](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/Maven-Overlay-Project-How-To) of the [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) server.

## Prerequisites

You will need the following installed on your system:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

## Setup and Installation

1. Add entries to your ```/etc/hosts``` file that map ```honest-op```, ```malicious-op```, and ```honest-client``` to your loopback interface. On my system this looks like:

    ```bash
    127.0.1.1       honest-op  
    127.0.1.1       malicious-op  
    127.0.1.1       honest-client
    ```

2. Build the project, supplying ```honest.issuer.uri``` for ```honest-op```, ```malicious.issuer.uri``` for ```malicious-op```, and ```honest.client.uri``` for ```honest-client```:

    ```bash
    mvn clean package -Dhonest.issuer.uri=http://honest-op/honest-op/ -Dmalicious.issuer.uri=http://malicious-op/malicious-op/ -Dhonest.client.uri=http://honest-client/honest-client/
    ```

    If packaging locally is not an option or you run into errors with local packaging, you can use Docker to build the packages:

    ```bash
    docker run --rm -it -v $(pwd):/project mvn clean package -Dhonest.issuer.uri=http://honest-op/honest-op/ -Dmalicious.issuer.uri=http://malicious-op/malicious-op/ -Dhonest.client.uri=http://honest-client/honest-client/ 
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

The honest server is accessible at [http://honest-op/honest-op/](http://honest-op/honest-op/).  
The malicious server is accessible at [http://malicious-op/malicious-op/](http://malicious-op/malicious-op/).  
The client app is accessible at [http://honest-client/honest-client/](http://honest-client/honest-client/).

The servers are set up by default with in-memory databases containing users `user`/`password` and `admin`/`password`.

## References

\[1\] [Mladenov, V., Mainka, C., & Schwenk, J. (2015). On the security of modern single sign-on protocols: Second-order vulnerabilities in openid connect. arXiv preprint arXiv:1508.04324](https://arxiv.org/pdf/1508.04324.pdf)
