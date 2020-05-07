# Oauth2 - Example

* Example of oauth2 usage with client, resource-server and authorization-server

Client and resource server were developed in [Spring Boot](https://projects.spring.io/spring-boot/).  
[Keycloak](https://www.keycloak.org/) was used as authorization server.  
Used [Docker](https://www.docker.com/) to contenerize environment.  
[MongoDB](https://www.mongodb.com/) was used to store some data in client and [PostgreSQL](https://www.postgresql.org/) was used to store Keycloak data

## Oauth2
![Oauth2 Diagram](https://user-images.githubusercontent.com/15820051/81329655-7b6b9980-9064-11ea-9996-79a87cd51896.png)

## Client
* Session has max inactive interval set, so in case of keycloak sso expiration, session in client application expires as well
It was implemented because keycloak session could have already expired and spring oauth2 did allowed user to access oauth2 protected endpoints and then fail when requesting resource-server. It would need to be manually implemented to every time check if token is valid, and if not redirect user to authorization server again. I choose more elegant way (less complex code)
* In case of user sending any additional headers (in this case: Authorization header), it is cached in mongoDB before redirection and retrieved after it, as it is lost.
* Token can expire, but it is possible to refresh it. As refreshed token will be lost at the end of the request, it is being saved to mongoDB to be used for further requests.
Client token refresh logic:
![Token Refresh Logic](https://user-images.githubusercontent.com/15820051/81329811-b1a91900-9064-11ea-92be-160e05a2fdda.png)
* Cached data (Headers and tokens) are present for 3 days in mongoDB.
 
## Resource Server
* Contains basic security configuration to check if client accessing it has proper permissions (scopes)

## Authorization Server (Keycloak)
* Used Basic configuration with one realm, one client, one user
* Disabled https requirement for test realm so client and resource server can access it (in production environment, it would need to be enabled and it should use valid certificate)

## Docker-compose
* Keycloak test realm is imported
* Ip's had to be used, as client and resource-server have to communicate with authorization server, and if container-names are used then client redirection to authorization-server for user(browser) will not work, as local computer (not container) cannot decrypt what does "http://keycloak" means. At the same time, localhost (which would work for local computer) couldn't be used as well, because it would disallow connection for user and resource-server. This was done only for example purposes, in production environment those server would have normal dns names which would be easly resolved.

## How to run the environment?

### Before you start
* Make sure you have `Docker` and `docker-compose` installed.

[Docker CE INSTALLATION](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
[Docker Compose INSTALLATION](https://docs.docker.com/compose/install/#prerequisites)

### Start the environment
1. Build services:
    `docker-compose build`
2. Run services:
    `docker-compose up`

### Important endpoints:
* https://172.1.1.2:8443 - Keycloak
* http://172.1.1.4:8081/oauth2/test - Client oauth2 protected endpoint
* http://172.1.1.4:8081/normal/test - Client not protected endpoint

