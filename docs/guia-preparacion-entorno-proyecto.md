# Guia de preparacion del entorno y descripcion tecnica del proyecto

## 1. Preparacion del entorno

### Requisitos previos

Para ejecutar y desarrollar CyberVoid S.L. se necesita:

- JDK 17 o superior.
- Maven 3.9 o superior.
- MySQL Server 8 o compatible.
- Git.
- Un IDE como IntelliJ IDEA, Eclipse, VS Code o Spring Tool Suite.
- Navegador web moderno.

El proyecto esta desarrollado con Spring Boot 3.3.5, por lo que Java 17 es la version minima recomendada.

### Configuracion de Java y Maven

Comprobar Java:

```powershell
java -version
```

Comprobar Maven:

```powershell
mvn -version
```

Si se trabaja en Windows y hay varias versiones de Java instaladas, se puede fijar temporalmente `JAVA_HOME`:

```powershell
$env:JAVA_HOME="C:\ruta\a\jdk-17"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

### Configuracion de MySQL

La aplicacion usa MySQL en desarrollo. La conexion se define en:

```text
src/main/resources/application.properties
```

Configuracion principal:

```properties
spring.datasource.url=${MYSQL_URL:jdbc:mysql://localhost:3306/cybervoid?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true}
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:root}
spring.jpa.hibernate.ddl-auto=update
```

Se pueden usar variables de entorno para no guardar credenciales reales en el codigo:

```powershell
$env:MYSQL_URL="jdbc:mysql://localhost:3306/cybervoid?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Europe/Madrid&allowPublicKeyRetrieval=true"
$env:MYSQL_USER="root"
$env:MYSQL_PASSWORD="tu_password"
```

La opcion `createDatabaseIfNotExist=true` permite crear la base de datos `cybervoid` si no existe. Hibernate actualiza las tablas con `spring.jpa.hibernate.ddl-auto=update`.

### Instalacion de dependencias

Desde la raiz del proyecto:

```powershell
mvn clean install
```

Si solo se quiere compilar:

```powershell
mvn compile
```

### Ejecucion local

Arrancar la aplicacion:

```powershell
mvn spring-boot:run
```

La aplicacion queda disponible en:

```text
http://localhost:8080
```

Credenciales iniciales creadas por `DataInitializer`:

```text
Administrador:
admin@cybervoid.local / admin123

Usuario:
alex.void@example.com / user123
```

### Ejecucion de pruebas

Las pruebas usan H2 en memoria con perfil `test`. La configuracion esta en:

```text
src/test/resources/application-test.properties
```

Ejecutar tests:

```powershell
mvn test
```

Crear un paquete ejecutable:

```powershell
mvn clean package
```

Ejecutar el JAR generado:

```powershell
java -jar target/cybervoid-1.0.0.jar
```

## 2. Estructura del proyecto

Estructura principal:

```text
Proyecto_CyberVoid/
├── pom.xml
├── index.html
├── login.html
├── usuario.html
├── carrito.html
├── mujer.html
├── hombre.html
├── ofertas.html
├── css/
├── js/
├── assets/
├── docs/
├── src/
│   ├── main/
│   │   ├── java/com/cybervoid/
│   │   │   ├── CyberVoidApplication.java
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
│       ├── java/com/cybervoid/
│       └── resources/
└── target/
```

El proyecto conserva dos partes:

- Prototipo estatico en la raiz: HTML, CSS y JS que pueden abrirse directamente en el navegador.
- Aplicacion Spring Boot en `src/`: backend real con MVC, seguridad, persistencia y plantillas Thymeleaf.

### Carpetas importantes

| Carpeta | Funcion |
|---|---|
| `src/main/java/com/cybervoid/controller` | Controladores MVC que reciben peticiones HTTP. |
| `src/main/java/com/cybervoid/service` | Logica de negocio de usuarios, productos, carrito, pedidos e informes. |
| `src/main/java/com/cybervoid/repository` | Acceso a base de datos con Spring Data JPA. |
| `src/main/java/com/cybervoid/model` | Entidades JPA del dominio. |
| `src/main/java/com/cybervoid/security` | Configuracion de autenticacion y autorizacion. |
| `src/main/java/com/cybervoid/dto` | Objetos de formulario y transferencia de datos. |
| `src/main/resources/templates` | Vistas Thymeleaf servidas por Spring Boot. |
| `src/main/resources/static` | CSS, JS e imagenes para la version Spring. |
| `css`, `js`, `assets`, `img` | Recursos del prototipo estatico. |
| `docs` | Documentacion tecnica del proyecto. |

## 3. Modulos desarrollados

### Modulo de catalogo

Permite mostrar productos por secciones:

- Inicio.
- Mujer.
- Hombre.
- Ofertas.
- Temporadas.

Clases principales:

- `ProductoController`
- `ProductoService`
- `ProductoRepository`
- `Producto`
- `Categoria`
- `Proveedor`

Flujo:

```text
GET /mujer
  -> ProductoController
  -> ProductoService
  -> ProductoRepository
  -> MySQL
  -> productos/catalogo.html
```

### Modulo de usuarios y autenticacion

Permite registrar usuarios e iniciar sesion con Spring Security.

Clases principales:

- `AuthController`
- `UsuarioController`
- `UsuarioService`
- `UsuarioRepository`
- `RegistroForm`
- `SecurityConfig`
- `CustomUserDetailsService`
- `Usuario`
- `Role`

Funciones:

- Registro de nuevos usuarios.
- Cifrado de contrasenas con BCrypt.
- Login con email y contrasena.
- Logout.
- Proteccion de rutas privadas.
- Perfil de usuario e historial de pedidos.

### Modulo de carrito

Gestiona un carrito persistente por usuario autenticado.

Clases principales:

- `CarritoController`
- `CarritoService`
- `CarritoRepository`
- `Carrito`
- `LineaCarrito`
- `Producto`

Funciones:

- Ver carrito.
- Anadir productos.
- Actualizar cantidades.
- Eliminar lineas.
- Calcular total.
- Validar stock.

### Modulo de pedidos y ventas

Convierte el carrito en un pedido confirmado.

Clases principales:

- `Pedido`
- `LineaPedido`
- `PedidoEstado`
- `Venta`
- `PedidoRepository`
- `VentaRepository`
- `CarritoService`

Funciones:

- Crear pedido.
- Copiar lineas del carrito al pedido.
- Mantener precio historico.
- Descontar stock.
- Registrar venta.
- Vaciar carrito.

### Modulo de administracion

Permite gestionar datos desde un panel restringido a administradores.

Clases principales:

- `AdminController`
- `ProductoRepository`
- `CategoriaRepository`
- `ProveedorRepository`
- `ClienteRepository`
- `PedidoRepository`
- `InformeRepository`
- `InformeService`

Funciones:

- Dashboard de administracion.
- Alta de productos.
- Listado de clientes, proveedores y pedidos.
- Generacion de informes.

### Modulo de informes

Genera informacion operativa del sistema.

Clases principales:

- `InformeService`
- `InformeRepository`
- `Informe`

Funciones:

- Contar productos activos.
- Contar pedidos.
- Crear resumen operativo.
- Guardar informes generados.

### Modulo frontend

Incluye dos variantes:

- HTML estatico en la raiz para prototipo visual.
- Plantillas Thymeleaf en `src/main/resources/templates` para la aplicacion real.

Archivos destacados:

- `index.html`
- `login.html`
- `usuario.html`
- `src/main/resources/templates/auth/login.html`
- `src/main/resources/templates/auth/registro.html`
- `src/main/resources/templates/fragments/layout.html`
- `src/main/resources/static/css/styles.css`
- `css/styles.css`

## 4. Codigo relevante explicado

### Clase principal

Archivo:

```text
src/main/java/com/cybervoid/CyberVoidApplication.java
```

Codigo:

```java
@SpringBootApplication
public class CyberVoidApplication {
    public static void main(String[] args) {
        SpringApplication.run(CyberVoidApplication.class, args);
    }
}
```

Explicacion:

- `@SpringBootApplication` activa la configuracion automatica de Spring Boot.
- Escanea los paquetes hijos de `com.cybervoid`.
- Arranca Tomcat embebido.
- Crea el contexto de Spring y registra controladores, servicios y repositorios.

### Configuracion de seguridad

Archivo:

```text
src/main/java/com/cybervoid/security/SecurityConfig.java
```

Codigo relevante:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/mujer", "/hombre", "/ofertas", "/temporada/**",
        "/css/**", "/js/**", "/img/**", "/assets/**", "/login", "/registro").permitAll()
    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
    .requestMatchers("/carrito/**", "/usuario/**", "/pedido/**").authenticated()
    .anyRequest().denyAll()
)
.formLogin(login -> login
    .loginPage("/login")
    .defaultSuccessUrl("/usuario", true)
    .permitAll()
)
```

Explicacion:

- Las paginas publicas se pueden ver sin iniciar sesion.
- `/admin/**` solo lo puede usar un administrador.
- `/carrito/**`, `/usuario/**` y `/pedido/**` requieren usuario autenticado.
- El formulario de login personalizado esta en `/login`.
- Tras iniciar sesion correctamente se redirige a `/usuario`.

### Carga de usuarios para login

Archivo:

```text
src/main/java/com/cybervoid/security/CustomUserDetailsService.java
```

Funcion:

```text
Spring Security recibe email y contrasena
  -> llama a loadUserByUsername(email)
  -> UsuarioRepository.findByEmail(email)
  -> compara BCrypt
  -> crea sesion autenticada
```

Esta clase conecta Spring Security con los usuarios guardados en la base de datos.

### Registro de usuario

Archivo:

```text
src/main/java/com/cybervoid/service/UsuarioService.java
```

Codigo relevante:

```java
usuario.setPassword(passwordEncoder.encode(form.getPassword()));
usuario.setRol(Role.ROLE_USER);
usuario.setActivo(true);
usuarioRepository.save(usuario);
```

Explicacion:

- La contrasena nunca se guarda en texto plano.
- Se cifra con BCrypt mediante `PasswordEncoder`.
- El usuario nuevo recibe el rol `ROLE_USER`.
- La entidad se guarda usando `UsuarioRepository`.

### DTO de registro

Archivo:

```text
src/main/java/com/cybervoid/dto/RegistroForm.java
```

Explicacion:

- No se usa directamente la entidad `Usuario` como formulario.
- `RegistroForm` contiene solo los campos necesarios para registrar.
- Incluye validaciones como email valido y longitud minima de contrasena.

Esto evita mezclar datos internos de base de datos con datos que introduce el usuario.

### Repositorio de productos

Archivo:

```text
src/main/java/com/cybervoid/repository/ProductoRepository.java
```

Metodos relevantes:

```java
List<Producto> findByActivoTrueOrderByNombreAsc();
List<Producto> findByActivoTrueAndCategoriaSlugOrderByNombreAsc(String slug);
List<Producto> findByActivoTrueAndPrecioAnteriorIsNotNullOrderByNombreAsc();
Optional<Producto> findBySku(String sku);
```

Explicacion:

- Spring Data JPA genera las consultas a partir del nombre del metodo.
- `findByActivoTrue...` evita mostrar productos desactivados.
- `CategoriaSlug` permite filtrar por categorias como `mujer`, `hombre` o temporadas.
- `PrecioAnteriorIsNotNull` identifica productos en oferta.

### Servicio de carrito

Archivo:

```text
src/main/java/com/cybervoid/service/CarritoService.java
```

Responsabilidad principal:

```text
Obtener carrito
  -> validar producto
  -> comprobar stock
  -> crear o actualizar linea
  -> guardar carrito
```

En la confirmacion:

```text
Carrito
  -> Pedido
  -> LineaPedido
  -> Venta
  -> descuento de stock
  -> carrito vacio
```

Es el servicio con mas logica de negocio, porque coordina varias entidades y repositorios.

### Inicializacion de datos

Archivo:

```text
src/main/java/com/cybervoid/service/DataInitializer.java
```

Explicacion:

- Implementa `CommandLineRunner`.
- Se ejecuta automaticamente al arrancar.
- Crea usuarios iniciales, categorias, proveedor, cliente y productos.
- Evita duplicados comprobando si ya existen datos.

Sirve para que el proyecto sea funcional desde el primer arranque.

### Plantillas Thymeleaf

Ejemplo:

```html
<form th:action="@{/login}" method="post">
  <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
  <input type="email" name="username" required>
  <input type="password" name="password" required>
</form>
```

Explicacion:

- `th:action="@{/login}"` genera una ruta valida dentro de Spring.
- El campo CSRF protege formularios POST.
- Spring Security espera `username` y `password` por defecto.
- En este proyecto `username` equivale al email del usuario.

## 5. Integracion continua opcional

El proyecto no necesita integracion continua para ejecutarse, pero es recomendable usar GitHub Actions para comprobar automaticamente que compila y que los tests pasan.

Archivo recomendado:

```text
.github/workflows/maven-tests.yml
```

Contenido:

```yaml
name: Maven Tests

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Setup Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 17
          cache: maven

      - name: Run tests
        run: mvn test
```

Que valida:

- Que el proyecto descarga dependencias correctamente.
- Que compila.
- Que los tests pasan.
- Que no se suben cambios que rompan el arranque basico de Spring.

Como las pruebas usan H2 en memoria, no hace falta levantar MySQL en la accion de CI para los tests actuales.

## 6. Control de versiones con Git

### Inicializar repositorio

Si el proyecto todavia no tiene repositorio Git:

```powershell
git init
```

Configurar identidad:

```powershell
git config user.name "Tu Nombre"
git config user.email "tu-email@example.com"
```

### Archivo `.gitignore`

Se recomienda ignorar archivos generados, temporales y configuraciones locales:

```gitignore
target/
.idea/
.vscode/
*.iml
*.log
.env
```

`target/` no debe versionarse porque Maven lo genera al compilar.

### Primer commit

Ver cambios:

```powershell
git status
```

Anadir archivos:

```powershell
git add .
```

Crear commit:

```powershell
git commit -m "Initial CyberVoid project"
```

### Flujo de trabajo recomendado

Crear una rama por funcionalidad:

```powershell
git checkout -b feature/login-usuario
```

Hacer cambios y revisarlos:

```powershell
git status
git diff
```

Ejecutar pruebas antes del commit:

```powershell
mvn test
```

Guardar cambios:

```powershell
git add .
git commit -m "Add user login screen"
```

Volver a la rama principal e integrar:

```powershell
git checkout main
git merge feature/login-usuario
```

### Conexion con GitHub

Crear repositorio remoto en GitHub y enlazarlo:

```powershell
git remote add origin https://github.com/usuario/proyecto-cybervoid.git
git branch -M main
git push -u origin main
```

Subir ramas:

```powershell
git push -u origin feature/login-usuario
```

### Buenas practicas de versionado

- Hacer commits pequenos y descriptivos.
- No subir `target/`, credenciales ni archivos `.env`.
- Ejecutar `mvn test` antes de subir cambios importantes.
- Usar ramas para funcionalidades o correcciones.
- Revisar `git diff` antes de cada commit.
- Documentar cambios relevantes en `docs/` o en el README.

Ejemplos de mensajes de commit:

```text
Add Spring Security login configuration
Create persistent shopping cart service
Document project setup and Git workflow
Fix user registration validation
```

## 7. Resumen final

CyberVoid S.L. queda organizado como una aplicacion web MVC con:

- Frontend estatico de prototipo.
- Backend Spring Boot.
- Vistas Thymeleaf.
- Seguridad con Spring Security.
- Persistencia con Spring Data JPA y MySQL.
- Tests con H2.
- Documentacion tecnica en `docs/`.
- Flujo recomendado de versionado con Git.

La aplicacion puede ejecutarse localmente con Maven, conectarse a MySQL y validarse con pruebas automatizadas mediante `mvn test`.
