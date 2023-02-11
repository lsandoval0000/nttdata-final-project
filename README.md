# Microservicios

Se definieron los siguientes microservicios:

-	1: Clientes
-	2: Ahorro
-	3: Cuenta Corriente
-	4: Plazo Fijo
-	2: Personal
-	3: Empresarial
-	4: Tarjeta de Crédito

Y se definió bajo sorteo los responsables de cada uno (según el número que aparece al costado del microservicio).

Definiéndose lo siguiente:

-	Wilder Castro Apaza (4) -> Plazo Fijo y Tarjeta de Credito
-	Danner Rebaza Rodríguez (1) -> Clientes
-	Jhonston Vigo Salinas (3) -> Cuenta Corriente y Empresarial
-	Luis Sandoval Arévalo (2) -> Ahorro y Personal

# Docker

## Comando de Docker para la BD
```
docker run --restart unless-stopped --name mysqldb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysql -e mysql
```

**Nota: Las bases de datos se deben crear manualmente.**

## Comandos Docker para servidor KeyCloak

```
docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.3 start-dev
```

- Configurar un nuevo realm: **nttdata-final-project-realm**
- Crear un nuevo cliente dentro del nuevo realm: **nttdata-final-project-client**
- Configurar el nuevo cliente para emplear autenticación basada en el servicio de cuentas.
- Issuer: **http://localhost:8181/realms/nttdata-final-project-realm**
- Issuer para tokens (cuando se emplean clientes como Postman): **http://localhost:8181/realms/nttdata-final-project-realm/protocol/openid-connect/token**
- Copiar el secret del nuevo cliente creado.
- En clientes, como Postman, asignar **openid offline_access** al ámbito del token.

## Comando Docker para Zipkin

```
docker run -d -p 9411:9411 openzipkin/zipkin
```