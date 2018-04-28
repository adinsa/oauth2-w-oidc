# oauth2-w-oidc

This project is an effort to replicate the results of Mainka et al. [1] on second-order vulnerabilities in OpenID Connect.

It is a Maven project with the following modules:

- ```honest-authsrv```- The honest OpenID Provider (OP).
- ```malicious-authsrv```- The malicious OpenID Provider (OP). 
- ```honest-client```- A client based off of MITREid Connect's [simple-web-app](https://github.com/mitreid-connect/simple-web-app).

Both ```honest-authsrv``` and ```malicious-authsrv``` are [overlays](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/Maven-Overlay-Project-How-To) of the [MITREid Connect](https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server) server.

## Prerequisites

You will need the following installed on your system:

- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

## Setup and Installation

1. Add entries to your ```/etc/hosts``` file that map ```honest-authsrv```, ```malicious-authsrv```, and ```honest-client``` to your loopback interface. On my system this looks like:

    ```bash
    127.0.1.1       honest-authsrv  
    127.0.1.1       malicious-authsrv  
    127.0.1.1       honest-client
    ```

2. Build the project, supplying ```honest.issuer.uri``` for ```honest-authsrv```, ```malicious.issuer.uri``` for ```malicious-authsrv```, and ```honest.client.uri``` for ```honest-client```:

    ```bash
    mvn clean package -Dhonest.issuer.uri=https://honest-authsrv/honest-authsrv/ -Dmalicious.issuer.uri=http://malicious-authsrv/malicious-authsrv/ -Dhonest.client.uri=http://honest-client/honest-client/
    ```

    If packaging locally is not an option or you run into errors with local packaging, you can use Docker to build the packages:

    ```bash
    docker run --rm -it -v $(pwd):/project mvn clean package -Dhonest.issuer.uri=http://honest-authsrv/honest-authsrv/ -Dmalicious.issuer.uri=http://malicious-authsrv/malicious-authsrv/ -Dhonest.client.uri=http://honest-client/honest-client/ 
    ```

    This will build the war files in the docker container and save them to the current directory on the host. The container will be disposed after the build completes.

## Usage

1. To activate configuration for one of the attacks, set environment variable ```SPRING_PROFILES_ACTIVE``` to one of the following values:

    ```broken-end-user-auth```- Broken End-User Authentication Attack 

    ```ssrf```- Server Side Request Forgery Attack 

    ```code-injection```- Code Injection Attack 

    ```dos```- Denial-of-Service Attack 

    The variable may be set in the ```.env``` file or you may set it in the shell (which will take precedence over what is in ```.env```).

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

The honest server is accessible at [http://honest-authsrv/honest-authsrv/](http://honest-authsrv/honest-authsrv/).  
The malicious server is accessible at [http://malicious-authsrv/malicious-authsrv/](http://malicious-authsrv/malicious-authsrv/).  
The client app is accessible at [http://honest-client/honest-client/](http://honest-client/honest-client/).

The servers are set up by default with in-memory databases containing users `user`/`password` and `admin`/`password`.

## References

\[1\] [Mladenov, V., Mainka, C., & Schwenk, J. (2015). On the security of modern single sign-on protocols: Second-order vulnerabilities in openid connect. arXiv preprint arXiv:1508.04324](https://arxiv.org/pdf/1508.04324.pdf)
