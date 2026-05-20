# CyberVoid S.L.

CyberVoid S.L. es una tienda online de moda alternativa/cyberpunk desarrollada con Java, Spring Boot, Spring MVC, Thymeleaf, Spring Security, Spring Data JPA, MySQL y Maven.

El proyecto incluye un prototipo HTML estatico en la raiz y una aplicacion Spring Boot completa en `src/`, con usuarios, autenticacion, catalogo persistente, carrito, pedidos, ventas, administracion e informes.

Repositorio:

```text
https://github.com/rober-vilben/cybervoid_s.l.git
```

## Tecnologias

- Java 17 o superior
- Spring Boot 3.3.5
- Spring MVC
- Spring Security
- Thymeleaf
- Spring Data JPA
- MySQL
- H2 para pruebas
- Maven
- Bootstrap 5

## Estructura

```text
Proyecto_CyberVoid/
├── pom.xml
├── README.md
├── index.html
├── login.html
├── usuario.html
├── css/
├── js/
├── assets/
├── docs/
├── src/
│   ├── main/
│   │   ├── java/com/cybervoid/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── schema.sql
│   │       ├── static/
│   │       └── templates/
│   └── test/
└── target/
```

Los HTML de la raiz son el prototipo estatico. La aplicacion real se ejecuta desde Spring Boot y usa las plantillas de `src/main/resources/templates`.

## Requisitos

Antes de ejecutar el proyecto instala:

- JDK 17 o superior
- Maven 3.9 o superior
- MySQL Server
- Git

Comprobar versiones:

```powershell
java -version
mvn -version
git --version
```

## Configuracion de base de datos

La conexion se configura en:

```text
src/main/resources/application.properties
```

Valores por defecto:

```properties
spring.datasource.url=${MYSQL_URL:jdbc:mysql://localhost:3306/cybervoid?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true}
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:123456}
```

Por defecto la aplicacion intenta conectar a MySQL con:

```text
Base de datos: cybervoid
Usuario: root
Password: 123456
```

Si tus credenciales son distintas, define variables de entorno antes de arrancar:

```powershell
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="tu_password"
```

Opcionalmente puedes definir la URL completa:

```powershell
$env:MYSQL_URL="jdbc:mysql://localhost:3306/cybervoid?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true"
```

El archivo `schema.sql` documenta la estructura de tablas, aunque Hibernate puede crear/actualizar el esquema con:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Ejecucion local

Desde la raiz del proyecto, donde esta `pom.xml`:

```powershell
mvn spring-boot:run
```

Abrir en el navegador:

```text
http://localhost:8080
```

Rutas utiles:

```text
http://localhost:8080/
http://localhost:8080/login
http://localhost:8080/registro
http://localhost:8080/usuario
http://localhost:8080/carrito
http://localhost:8080/admin
```

Credenciales iniciales:

```text
Administrador:
admin@cybervoid.local / admin123

Usuario:
alex.void@example.com / user123
```

## Pruebas

Ejecutar tests:

```powershell
mvn test
```

Las pruebas usan H2 en memoria con configuracion propia:

```text
src/test/resources/application-test.properties
```

## Empaquetado

Generar el JAR:

```powershell
mvn clean package
```

Ejecutar el JAR:

```powershell
java -jar target/cybervoid-1.0.0.jar
```

Si necesitas indicar credenciales de MySQL al ejecutar el JAR:

```powershell
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="123456"
java -jar target/cybervoid-1.0.0.jar
```

## Despliegue

### Opcion 1: Servidor con Java y MySQL

1. Instalar Java 17 o superior en el servidor.
2. Instalar MySQL y crear un usuario con permisos sobre la base de datos `cybervoid`.
3. Generar el JAR con `mvn clean package`.
4. Copiar el JAR al servidor.
5. Configurar variables de entorno:

```powershell
$env:MYSQL_URL="jdbc:mysql://localhost:3306/cybervoid?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true"
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="123456"
```

6. Ejecutar:

```powershell
java -jar target/cybervoid-1.0.0.jar
```

### Opcion 2: Despliegue desde GitHub

Clonar el repositorio:

```powershell
git clone https://github.com/rober-vilben/cybervoid_s.l.git
cd cybervoid_s.l
```

Compilar y probar:

```powershell
mvn test
mvn clean package
```

Arrancar:

```powershell
mvn spring-boot:run
```

## Subir el proyecto a GitHub

Si todavia no hay repositorio Git local:

```powershell
git init
git branch -M main
```

Anadir el remoto:

```powershell
git remote add origin https://github.com/rober-vilben/cybervoid_s.l.git
```

Revisar archivos:

```powershell
git status
```

Anadir y crear commit:

```powershell
git add .
git commit -m "Initial CyberVoid project"
```

Subir a GitHub:

```powershell
git push -u origin main
```

Si el repositorio remoto ya tiene contenido y GitHub rechaza el push, primero sincroniza:

```powershell
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## Variables de entorno recomendadas

| Variable | Uso | Ejemplo |
|---|---|---|
| `MYSQL_URL` | URL JDBC de MySQL | `jdbc:mysql://localhost:3306/cybervoid?...` |
| `MYSQL_USER` | Usuario de MySQL | `root` |
| `MYSQL_PASSWORD` | Password de MySQL | `123456` |

## Documentacion adicional

La guia tecnica completa esta en:

```text
docs/guia-preparacion-entorno-proyecto.md
```

Tambien hay documentacion ampliada en:

```text
docs/README.md
```
