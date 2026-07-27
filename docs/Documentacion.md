# Biblioteca Digital — Documentación

## 1. Descripción general del sistema

Biblioteca Digital es una aplicación de escritorio en Java (Swing) para gestionar el
préstamo de libros de una biblioteca. Cubre tres entidades centrales — **usuarios**,
**libros** y **préstamos** — y dos roles de acceso con permisos distintos:

- **Administrador (`Admin`)**: alta/baja/modificación de libros y usuarios, generación
  y devolución de préstamos a nombre de cualquier usuario, y visualización de reportes
  (libros más solicitados, usuarios con más préstamos).
- **Usuario regular (`UsuarioRegular`)**: puede ver sus propios préstamos activos,
  generar un préstamo sobre un libro disponible, devolverlo y modificar su propio perfil.

El flujo de uso es: `PanelLogin` valida usuario/contraseña contra la base y, según el
`rol` almacenado, deriva a `PanelMenuAdmin` o `PanelMenuUsuario`; desde ahí cada botón
abre el panel de gestión correspondiente dentro de la misma ventana (`VentanaPrincipal`
reemplaza el panel visible con `cambiarPanel()`, no abre ventanas nuevas).

### Arquitectura por capas

El código está organizado en cuatro capas con una dirección de dependencia única
(las capas superiores dependen de las inferiores, nunca al revés):

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Presentación | `views` | Ventanas y paneles Swing; solo arma la UI y delega en `BibliotecaServices` |
| Servicios | `services` | Reglas de negocio y validaciones; única capa que lanza excepciones de negocio |
| Persistencia | `dao`, `dao.impl`, `util` | Acceso a MySQL vía JDBC; `ConexionDB` administra la conexión |
| Modelo | `model` | Entidades del dominio: `Usuario` (abstracta), `Admin`, `UsuarioRegular`, `Libro`, `Prestamo` |

Cada libro tiene una cantidad total y una disponible (`Libro.prestar()` /
`Libro.devolver()` las ajustan); cada usuario acumula préstamos actuales y totales
(usados para el reporte de "usuarios con más préstamos"); cada préstamo referencia a
un `Usuario` y un `Libro` y se marca `finalizado` en vez de borrarse al devolverse.

## 2. Justificación de las decisiones de diseño

**`Usuario` abstracta con `Admin` y `UsuarioRegular` como subclases.**
Ambos roles comparten casi todos los atributos (nombre, credenciales, contador de
préstamos), pero difieren en una sola operación: `generarPrestamo(Libro)`. En vez de
tener una única clase `Usuario` con un `if (rol.equals("ADMIN"))` disperso por el
código, esa diferencia se resuelve con polimorfismo — cada subclase implementa su
propia versión, y `Admin` además expone `generarPrestamoANombre(Usuario, Libro)`,
una operación que no tiene sentido para un usuario regular. El costo es que el campo
`rol` (String) igual se persiste en la tabla `usuario` para poder reconstruir el tipo
concreto correcto al leer de la base (`UsuarioDAOImpl.leer()` decide entre `new Admin(...)`
o `new UsuarioRegular(...)` según ese campo) — es la solución más simple dado que JDBC
no puede instanciar polimórficamente por sí solo.

**DAO separado de su implementación (`LibroDAO` interfaz + `LibroDAOImpl` clase).**
`BibliotecaServices` y los paneles de `views` dependen únicamente de las interfaces
DAO, nunca de `LibroDAOImpl`/`UsuarioDAOImpl`/`PrestamoDAOImpl` directamente (ver
`Main.java`, donde se instancian las impl concretas una sola vez y se inyectan por
constructor). Esto aísla el resto del sistema del detalle "hoy la persistencia es
JDBC contra MySQL": si mañana cambiara el motor o se agregara una capa de caché,
solo se reescribe la implementación, sin tocar reglas de negocio ni UI.

**Toda la lógica de negocio centralizada en `BibliotecaServices`, no en los DAO ni en
la UI.** Los DAO solo hacen CRUD contra la tabla que les corresponde; no saben qué es
un "libro ya existente" o un "préstamo activo". Esas validaciones (¿el libro ya existe
por título?, ¿el usuario está activo?, ¿hay stock disponible?) viven en un único lugar
(`BibliotecaServices`), así los paneles no repiten la misma regla de negocio cada vez
que arman un formulario, y un cambio de regla se edita en un solo método.

**Excepciones personalizadas en vez de devolver `null`/`boolean` o lanzar
`RuntimeException` genérica.** Antes de esta iteración, un fallo de JDBC se tragaba
con `e.printStackTrace()` y el método devolvía `null`, lo cual terminaba explotando
como `NullPointerException` más abajo, ocultando la causa real. Con excepciones con
nombre (`LibroYaExisteException`, `PrestamoNoActivoException`, `PersistenciaException`,
etc.) cada panel puede capturar exactamente el caso que le interesa y mostrarle al
usuario un mensaje que explica qué pasó, en vez de que la aplicación falle en
silencio o con una traza indescifrable.

**Un único punto de acceso a la conexión JDBC (`ConexionDB`).** Abrir una conexión
nueva por cada operación es costoso y, en una app de escritorio de un solo usuario,
innecesario: alcanza con una conexión compartida que vive mientras la app está
abierta. De ahí el Singleton (ver sección 3) en vez de que cada DAO abra la suya.

## 3. Explicación de los patrones de diseño utilizados

### Singleton — `util/ConexionDB.java`
Es el patrón pedido explícitamente por la consigna. `ConexionDB` tiene constructor
privado y expone `getInstance()` estático con *double-checked locking* (`synchronized`
solo en la primera creación, no en cada llamada) para que sea seguro incluso si
Swing disparara accesos desde más de un hilo. `getConnection()` además revisa si la
conexión guardada está cerrada (`connection.isClosed()`) y la reabre si hace falta,
en vez de devolver una conexión muerta — esto es lo que se corrigió cuando los DAO
cerraban la conexión compartida en cada `try-with-resources` y rompían el Singleton
para toda llamada subsiguiente.

```java
public Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
    return connection;
}
```

### DAO (Data Access Object) — `dao/` + `dao/impl/`
Patrón opcional de la consigna, implementado para las tres entidades principales.
`LibroDAO`, `UsuarioDAO` y `PrestamoDAO` declaran el contrato de persistencia (crear,
leer, actualizar, eliminar, más búsquedas específicas como `buscarPorTitulo` o
`librosMasSolicitados`); `LibroDAOImpl`, `UsuarioDAOImpl` y `PrestamoDAOImpl` son la
única parte del sistema que sabe que la persistencia es JDBC/MySQL. `BibliotecaServices`
recibe las tres interfaces por constructor (inyección de dependencias manual, sin
framework), lo que además hace posible reemplazarlas por una implementación falsa en
tests sin tocar el resto del sistema.

### Lo que la consigna deja como opcional y no se implementó
- **DTO**: no se usa; las entidades de `model` se pasan directamente entre capas.
  Se podría justificar en un sistema de esta escala porque no hay una API externa ni
  una vista que necesite un subconjunto distinto de campos — agregar DTOs hoy
  duplicaría cada clase sin resolver un problema real.
- **Un patrón adicional (Observer/Factory/Strategy)**: no está implementado. El caso
  más cercano sería una *Factory* para construir `Admin` o `UsuarioRegular` según el
  campo `rol` al leer de la base (hoy ese `if/else` vive inline en `UsuarioDAOImpl`),
  pero no se llegó a extraer como patrón formal.
