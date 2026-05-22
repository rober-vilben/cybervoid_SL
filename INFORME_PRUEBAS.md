# Informe de pruebas - CyberVoid S.L.

## Alcance analizado

Se ha revisado el backend Spring Boot existente bajo `src/main/java/com/cybervoid`, incluyendo:

- Entidades JPA: `Usuario`, `Producto`, `Categoria`, `Proveedor`, `Cliente`, `Carrito`, `LineaCarrito`, `Pedido`, `LineaPedido`, `Venta` e `Informe`.
- Repositorios Spring Data JPA: productos, usuarios, carritos, pedidos, ventas, informes, clientes, categorias y proveedores.
- Servicios: `ProductoService`, `UsuarioService`, `CarritoService`, `InformeService`, `CustomUserDetailsService` y `DataInitializer`.
- Controladores MVC: `HomeController`, `ProductoController`, `AuthController`, `CarritoController`, `UsuarioController` y `AdminController`.
- Seguridad: `SecurityConfig` con login formulario, rutas publicas, rutas autenticadas y area admin con `ROLE_ADMIN`.

Las pruebas se han creado solo sobre funcionalidades presentes en el codigo actual.

## Funcionalidades probadas

- Consulta de catalogo activo, por categoria y ofertas.
- Busqueda de producto existente y error cuando no existe.
- Registro de usuario con email normalizado, password codificada y rol `ROLE_USER`.
- Rechazo de registro con email duplicado.
- Busqueda de usuario por email normalizado.
- Carga de usuarios para Spring Security, autoridades y estado activo/inactivo.
- Creacion de carrito cuando no existe.
- Anadir productos al carrito, incrementar cantidades y validar stock.
- Actualizacion de cantidades con minimo 1.
- Eliminacion de lineas del carrito.
- Confirmacion de pedido desde carrito, calculo de total, reduccion de stock, creacion de venta y vaciado del carrito.
- Rechazo de confirmacion de pedido con carrito vacio.
- Generacion de informe operativo con conteo de pedidos y productos activos.
- Controladores publicos de catalogo, ofertas, temporadas, login y registro.
- Validacion MVC del formulario de registro y tratamiento de email duplicado.
- Consultas derivadas de `ProductoRepository` con H2 mediante `@DataJpaTest`.
- Seguridad de rutas ya cubierta por la prueba de integracion existente `CyberVoidApplicationTests`.

## Objetivos cubiertos por las pruebas

- OBJ-001: Centralizacion del sistema, mediante servicios, repositorios y controladores integrados.
- OBJ-002: Gestion de catalogo de productos, mediante consultas de producto y vistas de catalogo.
- OBJ-004: Seguimiento de pedidos, parcialmente cubierto por consulta de pedidos en usuario y creacion de pedido confirmado.
- OBJ-005: Gestion de clientes/usuarios, mediante registro y carga de usuario autenticable.
- OBJ-006: Experiencia de usuario, mediante rutas publicas y redirecciones de registro.
- OBJ-008: Seguridad del sistema, mediante autenticacion, roles y `UserDetailsService`.
- OBJ-009: Generacion de informes, mediante `InformeService`.
- OBJ-012: Automatizacion de procesos internos, mediante confirmacion automatica de pedido y venta.
- OBJ-013: Integridad de datos, mediante validaciones de stock, email duplicado y constraints JPA probados en slices.
- OBJ-014: Acceso remoto, cubierto de forma indirecta por controladores web MVC.
- OBJ-015: Adaptacion al entorno digital, cubierto de forma indirecta por catalogo web, registro y carrito.

## Requisitos funcionales relacionados

- RF-001: Autenticacion de usuarios.
- RF-002: Gestion de usuarios.
- RF-003: Control de acceso por roles.
- RF-004: Gestion y consulta de productos.
- RF-006: Gestion/consulta de categorias en catalogo.
- RF-007: Consulta del catalogo.
- RF-009: Historial de compras, parcialmente implementado en `UsuarioController`.
- RF-011: Creacion de pedidos desde carrito.
- RF-012: Seguimiento de pedidos, parcialmente implementado mediante historial de usuario.
- RF-014: Registro de ventas al confirmar pedido.
- RF-015: Calculo automatico del total.
- RF-017: Generacion de informes.
- RF-018: Busqueda y filtrado mediante repositorios derivados.
- RF-021: Gestion centralizada del negocio en panel admin.
- RF-023: Validacion de datos.
- RF-024: Integracion con base de datos.
- RF-025: Automatizacion de procesos internos.

## Clases de prueba creadas

- `src/test/java/com/cybervoid/service/ProductoServiceTest.java`
- `src/test/java/com/cybervoid/service/UsuarioServiceTest.java`
- `src/test/java/com/cybervoid/service/CarritoServiceTest.java`
- `src/test/java/com/cybervoid/service/InformeServiceTest.java`
- `src/test/java/com/cybervoid/security/CustomUserDetailsServiceTest.java`
- `src/test/java/com/cybervoid/controller/ProductoControllerTest.java`
- `src/test/java/com/cybervoid/controller/AuthControllerTest.java`
- `src/test/java/com/cybervoid/repository/ProductoRepositoryTest.java`

Tambien se mantiene la prueba existente:

- `src/test/java/com/cybervoid/CyberVoidApplicationTests.java`

## Funcionalidades documentadas pero no implementadas de forma testeable

- RF-005 Gestion de variantes de producto: el modelo `Producto` contiene talla y color, pero no existe entidad, servicio ni controlador especifico de variantes.
- RF-008 Gestion de clientes: existe entidad y repositorio `Cliente`, y se muestra en admin, pero no hay servicio ni CRUD MVC dedicado.
- RF-010 Gestion de proveedores: existe entidad y repositorio `Proveedor`, y se muestra en admin, pero no hay servicio ni CRUD MVC dedicado.
- RF-013 Actualizacion de estado de pedidos: existe `PedidoEstado` y setter en `Pedido`, pero no hay servicio/controlador para transiciones de estado.
- RF-016 Generacion de ticket: no se ha encontrado servicio, controlador, DTO ni vista especifica de ticket.
- RF-022 Registro de operaciones: no existe auditoria, log funcional persistido ni entidad de operaciones.
- OBJ-003 Automatizacion de pedidos dropshipping y OBJ-007 Integracion con proveedores: existen proveedores asociados a productos, pero no hay integracion externa ni flujo dropshipping real.
- OBJ-010 Escalabilidad: no hay componentes especificos de cache, colas, paginacion avanzada o arquitectura distribuida que puedan probarse.

## Recomendaciones

- Crear servicios dedicados para clientes, proveedores y pedidos si se van a gestionar desde el panel admin; despues anadir pruebas unitarias de CRUD y validaciones.
- Incorporar un flujo explicito de cambio de estado de pedido para cubrir RF-013.
- Separar variantes de producto en entidad propia si RF-005 debe ser un requisito completo.
- Implementar ticket o justificante de venta antes de probar RF-016.
- Reducir el ruido de logs SQL/debug en el perfil de test para que la salida de Maven sea mas legible.

## Verificacion

Comando ejecutado:

```bash
mvn test
```

Resultado:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
