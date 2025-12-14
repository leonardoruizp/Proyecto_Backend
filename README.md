# Despliegue y compilación local y en Railway del Backend del Proyecto

El siguiente documento explica los pasos necesarios para que se pueda levantar el backend del proyecto de forma local y en Railway

## 1. Requisitos Previos
- Java 17
- Maven 3.9+
- Git
- CURL para realizar las pruebas desde consola
- MariaDB/MySQL para correrlo localmente
- Clonar el repositorio y estar en la rama Master

``` bash
git clone
cd Proyecto_Backend
```

---

## 2. Despliegue y compilación local

### 2.1 Editar archivo application.properties

Editar el archivo que se encuentra en la ruta  `src/main/resources/application.properties` para configurar la base de datos local

Agregar las siguientes líneas:
``` bash
spring.datasource.url=jdbc:mariadb://${MYSQL_HOST:localhost}:3306/consultoriodental?servertimeZone=UTC
spring.datasource.username=root
spring.datasource.password=123
```

### 2.2 Compilar la base de datos usando contenedores

Usar la imagen de mariadb y los contenedores en Docker para compilar nuestra base de datos y correr en consola el siguiente comando

```bash
docker run -it --rm -p 3307:3306 -v "c:\Users\leona\Proyecto_Backend":/var/lib/mysql --env MARIADB_ROOT_PASSWORD=123 --name=bd mariadb
```

### 2.3 Obtener la dirección IP del contenedor

Comando:

```bash
docker exec -it bd sh -c "cat /etc/hosts" | tail -n 1 | awk '{print $1}' 
```

### 2.4 Desplegar el servicio

El siguiente paso es desplegar nuestro servicio usando la imagen de spring-boot, para ello vamos a ejecutar el siguiente comadno en consola

```bash
docker run -it --rm -v $(pwd -W):/app -v "c:\Users\leona\.m2":/root/.m2 -p 8080:8080 --env MYSQL_HOST=172.17.0.2 rrojano/spring-boot
```

Dentro nos ubicamos en `consultoriodental` con el comando:

```bash
cd consultoriodental
```

Corremos el servicio con el comando:
```bash
./mvnw spring-boot:run
```

### 2.5 Acceso a la base de datos

Acceder a la base de datos usando el siguiente comando:

```bash
docker exec -it bd mariadb -u root -p
password: 123
```
O en: `http://localhost:8080`

## 3. Despliegue en Railway

### 3.1 Crear un proyecto en Railway

- Entrar a: `https://railway.app`
- Crear un nuevo servicio usando nuestro repositorio de Github.

### 3.1 Editar archivo application.properties

Editar el archivo que se encuentra en la ruta  `src/main/resources/application.properties` para configurar la base de datos para Railway y usando FreeDB

Agregar las siguientes líneas:
``` bash
spring.datasource.url=jdbc:mariadb://${MYSQL_HOST:sql.freedb.tech}:3306/freedb_MiBase?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=freedb_leonardo
spring.datasource.password=58Tt6am&%JDHd3?
```

### 3.2 Dockerfile

Asegurarnos de que exista nuestro Dockerfile en nuestro repositorio, para que Railway lo detecte automáticamente, con la siguiente información:

```bash
# Etapa 1: construir el JAR
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY consultoriodental/pom.xml .
RUN mvn -q dependency:go-offline
COPY consultoriodental/src ./src
RUN mvn -q -DskipTests clean package

# Etapa 2: imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3.3 Esperar a que Railway construya el contenedor

### 3.4 Usar el dominio público

Railway nos da una URL como la siguiente:

`https://proyectobackend-production-19cb.up.railway.app`

## 4. Pruebas con CURL

### 4.1 Local

Obtener citas usando GET:

```bash
curl -X GET http://localhost:8080/citas
```

Crear una cita usando POST:

```bash
curl -X POST -H "Content-Type:application/json" -d '{"paciente":"Juan Perez","motivo":"Limpieza","fecha":"2025-01-10","hora":"10:00"}' http://localhost:8080/citas
```

Actualizar una cita usando PUT:

```bash
curl -X PUT -H "Content-Type:application/json" -d '{"paciente":"Juan Perez","motivo":"Extracción","fecha":"2025-01-11","hora":"12:00","estado":"pendiente"}' http://localhost:8080/citas/1
```

Eliminar una cita usando DELETE:

```bash
curl -X DELETE http://localhost:8080/citas/1
```
### 4.2 Railwway

Obtener citas usando GET:

```bash
curl -X GET https://proyectobackend-production-0ab7.up.railway.app/citas
```

Crear una cita usando POST:

```bash
curl -X POST -H "Content-Type:application/json" -d '{"paciente":"Juan Perez","motivo":"Limpieza","fecha":"2025-01-10","hora":"10:00"}' https://proyectobackend-production-0ab7.up.railway.app/citas
```

Actualizar una cita usando PUT:

```bash
curl -X PUT -H "Content-Type:application/json" -d '{"paciente":"Juan Perez","motivo":"Extracción","fecha":"2025-01-11","hora":"12:00","estado":"pendiente"}' https://proyectobackend-production-0ab7.up.railway.app/citas/1
```

Eliminar una cita usando DELETE:

```bash
curl -X DELETE https://proyectobackend-production-0ab7.up.railway.app/citas/1
```
