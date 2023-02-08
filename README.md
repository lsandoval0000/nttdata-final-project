# Docker

## Docker command for BD
```
docker run --restart unless-stopped --name mysqldb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -e mysql
```

**Note: Databases need to be created manually.**

## Docker command for KeyCloak server

```
docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.3 start-dev
```

- Configure new realm: **nttdata-final-project-realm**
- Create new client inside new realm: **nttdata-final-project-client**
- Configure new client to use authentication based on account services
- Issuer must be: **http://localhost:8181/realms/nttdata-final-project-realm**
- Issuer for tokens must be (for clients like Postman): **http://localhost:8181/realms/nttdata-final-project-realm/protocol/openid-connect/token**
- Copy the secret from the new client.
- On clients (like Postman), set **openid offline_access** as a scope for tokens

## Docker command for Zipkin

```
docker run -d -p 9411:9411 openzipkin/zipkin
```