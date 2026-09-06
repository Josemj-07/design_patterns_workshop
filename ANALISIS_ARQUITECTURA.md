# Análisis de arquitectura, flujo de ejecución, SOLID y patrones
### Proyecto: `NuevaArquitectura/FarmaciaSolidJava`

> Documento de análisis. La restricción de partida es que **la salida por
> terminal no debe cambiar en absoluto**.
>
> **Actualización 2026-09-06:** las secciones 1–8 son el análisis original (redactado
> cuando aún no se había tocado el código). Las correcciones descritas **ya fueron
> aplicadas** — ver **§9. Correcciones aplicadas** al final.

Fecha: 2026-09-06
Compilación verificada: `javac` (OpenJDK 21) → **OK, 0 errores**.
Ejecución verificada: la aplicación arranca, hace login, muestra menú y opera.
Salida por terminal: **verificada byte a byte contra el commit `82cf142`** (login OK,
login fallido, opciones 1–7, ventas que dejan stock negativo, búsquedas parciales y
fallidas, puntos negativos, opciones inválidas) → **idéntica**.

---

## 1. Flujo de ejecución

### 1.1 Cadena de arranque (composition root)

```
Main.main()
 ├─ resolverCarpetaDatos()                         (localiza ./datos)
 ├─ new AplicacionFarmaciaBuilder()   ── ConcreteBuilder
 ├─ new EnsambladorAplicacion()       ── Director
 ├─ ensamblador.ensamblarAplicacionCompleta(constructor)
 │     ├─ constructor.comenzar()
 │     ├─ constructor.construirInfraestructura()   → AdaptadorFuenteTextoArchivo,
 │     │                                             RegistradorFabricasIPagable,
 │     │                                             NotificadorConsola
 │     ├─ constructor.construirRepositorios()      → 4 repos en memoria +
 │     │                                             AutenticacionPorCredenciales
 │     ├─ constructor.construirCasosDeUso()        → 11 casos de uso envueltos en
 │     │                                             AplicacionFarmacia (Facade)
 │     └─ constructor.obtenerResultado()           → AplicacionFarmacia
 └─ new MenuFarmacia(aplicacionFarmacia, rutaProductos, rutaClientes, rutaUsuarios)
       └─ menuFarmacia.ejecutar()
```

### 1.2 Ejecución interactiva (`MenuFarmacia.ejecutar()`)

1. **Carga de datos** — imprime el resultado de:
   `aplicacionFarmacia.cargarProductos(...)` → `CargarPagables.ejecutar()`
   `aplicacionFarmacia.cargarClientes(...)` → `CargarClientes.ejecutar()`
   `aplicacionFarmacia.cargarUsuarios(...)` → `CargarUsuarios.ejecutar()`
2. **Login** — `aplicacionFarmacia.iniciarSesion()` → `AutenticarUsuario` →
   `IAutenticacion.login()` → `AutenticacionPorCredenciales` →
   `IRepositorioUsuario.buscarPorNombreUsuario()`.
3. **Alertas iniciales** — `aplicacionFarmacia.verificarAlertas()` →
   `VerificarAlertas.verificarStock()` + `verificarVencimiento()` →
   `INotificador.stockMinimo()` / `vencimiento()`.
4. **Bucle de menú** (opciones 1–7):
   | Opción | Método menú | Caso de uso | Puerto(s) |
   |---|---|---|---|
   | 1 Ver productos | `listarProductos()` | `ListarPagables` | `IRepositorioPagable` |
   | 2 Ver clientes | `listarClientes()` | `ListarClientes` | `IRepositorioCliente` |
   | 3 Buscar producto | `buscarProducto()` | `BuscarPagable` | `IRepositorioPagable` |
   | 4 Registrar venta | `registrarVenta()` | `BuscarPagable` + `RegistrarVenta` | `IRepositorioPagable`, `IRepositorioVenta`, `INotificador` |
   | 5 Acumular puntos | `acumularPuntos()` | `BuscarCliente` + `AcumularPuntos` | `IRepositorioCliente`, `INotificador` |
   | 6 Ver alertas | — | `VerificarAlertas` | `IRepositorioPagable`, `INotificador` |
   | 7 Salir | — | — | — |

### 1.3 Capas y dirección de dependencias

```
ui  ──►  aplicacion  ──►  dominio  ◄──  infraestructura
(MenuFarmacia)   (casos de uso,    (entidades,     (adaptadores que
                  Facade, factories) puertos)        implementan puertos)
```

`dominio/puertos/*` **no importa nada de `infraestructura`** ni de `ui`: la
inversión de dependencias está bien resuelta y la composición se hace en un
único lugar (`AplicacionFarmaciaBuilder`).

### 1.4 ¿El flujo es correcto?

**Sí, funcionalmente el flujo es coherente y se ejecuta de principio a fin**,
con una excepción importante (regresión de salida) y varios puntos frágiles:

| # | Severidad | Hallazgo |
|---|---|---|
| F1 | **Alta — rompe la restricción de salida** | `MenuFarmacia.listarProductos()` cambió el formato impreso (ver §2.1). |
| F2 | **Alta — bug latente** | `RegistrarVenta` hace `(Producto) productoEncontrado.get()`; si el pagable es un `Servicio` → `ClassCastException` no capturada (ver §2.2). |
| F3 | Media | La vista "Ver productos" y "Buscar producto" mostrarán también `Servicio` cuando existan, mezclados con productos (ver §5.4). |
| F4 | Media | `MenuFarmacia` y `VerificarAlertas` dependen de `instanceof Producto` + *downcast*; la abstracción `IPagable` es demasiado pobre para lo que consumen (ver §4 ISP/OCP). |
| F5 | Baja | `ServicioFactory` no valida `campos.length`; línea `SER;` incompleta lanza `ArrayIndexOutOfBoundsException` (se degrada a mensaje genérico). |
| F6 | Baja | Imports muertos tras el refactor (`IRepositorioPagable` y `IPagableFactory` importan `Producto` sin usarlo). |
| F7 | Baja | Incoherencia de nombres: `AplicacionFarmacia.listarProductos()/buscarProducto()`, campo `casoUsoListarProductos` de tipo `ListarPagables`, etc. |
| F8 | Baja | Índice de git inconsistente: `dominio/IPagable.java` aparece *staged* como *new* y `dominio/interfaces/` sin trackear. En disco solo existe `dominio/interfaces/IPagable.java` (compila), pero el *commit* quedaría a medias. |
| F9 | Baja | Ubicación de paquetes: la fachada de aplicación y el ensamblado viven bajo `ui/` (ver §3). |

---

## 2. Regresiones / bugs frente al AS-IS

### 2.1 F1 — `listarProductos()` cambia la salida (CRÍTICO para la restricción)

**AS-IS (commit `82cf142`)** — `src/farmacia/ui/MenuFarmacia.java`:

```java
for (Producto producto : aplicacionFarmacia.listarProductos()) {
    System.out.println(
            producto.getNombre() + "\t\t" + producto.getStock() + "\t" + producto.getPrecio());
}
```
Salida:
```
Dolex		2	5000
Ibuprofeno		10	7000
```

**Estado actual** — `src/farmacia/ui/MenuFarmacia.java:113-124`:

```java
for (IPagable producto : aplicacionFarmacia.listarProductos()) {
    System.out.println(
            producto.getNombre() + "\t\t" + "\t" + producto.getPrecio());   // falta el stock, sobra un \t

    if (producto instanceof Producto) {
        Producto p = (Producto) producto;
        System.out.println(p.getStock());                                    // stock en LÍNEA APARTE
    }
}
```
Salida (verificada ejecutando):
```
Dolex			5000
2
Ibuprofeno			7000
10
```

Esto **viola explícitamente** "la salida en la terminal no debe cambiar
absolutamente nada". El origen es correcto (se quitó `getStock()` de la interfaz
`IPagable` porque un servicio no tiene stock), pero la reescritura del bucle
quedó mal.

**Sugerencia (no aplicada).** Reconstruir exactamente la línea del AS-IS para el
caso `Producto`, sin `println` intermedios:

```java
private void listarProductos() {
    System.out.println(COLOR_CYAN + "\n===== PRODUCTOS =====" + COLOR_RESET);
    System.out.println("Nombre\t\tStock\tPrecio");
    System.out.println("-----------------------------------");
    for (IPagable pagable : aplicacionFarmacia.listarProductos()) {
        if (pagable instanceof Producto p) {
            System.out.println(p.getNombre() + "\t\t" + p.getStock() + "\t" + p.getPrecio());
        } else {
            // Formato para servicios: se define cuando existan; hoy datos/productos.txt
            // no contiene líneas SER, por lo que la salida no cambia.
            System.out.println(pagable.getNombre() + "\t\t" + "\t" + pagable.getPrecio());
        }
    }
}
```

Mejor aún: eliminar el `instanceof` con una solución polimórfica (ver §5.3).

### 2.2 F2 — `RegistrarVenta` castea a `Producto` sin comprobar

`src/farmacia/aplicacion/casosdeuso/RegistrarVenta.java:36-38`:

```java
Producto productoAVender = (Producto) productoEncontrado.get();  // ClassCastException si es Servicio
productoAVender.descontarStock(cantidadVendida);
Venta venta = new Venta(LocalDateTime.now(), cantidadVendida, productoAVender);
```

El repositorio devuelve `IPagable`, pero el caso de uso asume `Producto`.
Como `datos/productos.txt` hoy no tiene servicios, no se dispara; pero **el
objetivo del proyecto es justamente vender servicios**, y ese camino está roto.
Además `descontarStock` no tiene semántica para un servicio.

Esto es una **violación de LSP**: `Servicio` es un `IPagable` que no es
sustituible donde el código espera un `IPagable` (revienta).

**Sugerencia (no aplicada).** Ver §5.3 — mover el "efecto de la venta sobre el
inventario" a un método polimórfico de `IPagable` (o usar Visitor), de modo que
`Producto` descuente stock y `Servicio` no haga nada, sin `cast`.

---

## 3. Estructura de paquetes / "screaming architecture"

El refactor movió piezas de aplicación a la capa de presentación:

| Antes (`82cf142`) | Ahora | Comentario |
|---|---|---|
| `aplicacion/fachada/AplicacionFarmacia` | `ui/facade/AplicacionFarmacia` | La **fachada de la capa de aplicación** ahora vive dentro de una UI concreta. |
| `aplicacion/construccion/*` (Builder/Director) | `ui/construccion/*` | El **ensamblado de la app** no es responsabilidad de la presentación. |
| `infraestructura/fabricas/*` | `aplicacion/fabricas/*` | Defendible: las factories solo reciben `String[]`, no tocan I/O. |
| `aplicacion/puertos/*` | `dominio/puertos/*` | **Mejora**: los puertos ahora pertenecen al dominio (hexagonal más puro). |
| `dominio/IPagable` | `dominio/interfaces/IPagable` | Cambio cosmético; el subpaquete `interfaces` es redundante con `puertos`. |

**Sugerencias (no aplicadas):**

1. Devolver `AplicacionFarmacia` a `aplicacion/fachada` (o `aplicacion/servicios`).
   Argumento: si mañana existe una UI web, **ambas UIs** deben compartir la
   fachada; no puede vivir dentro de `ui.consola`.
2. Mover `ui/construccion/*` a un paquete de arranque (`farmacia.arranque` o
   `farmacia.composicion`), junto a `Main`. El *composition root* es
   infraestructura de arranque, no presentación.
3. Unificar `dominio/interfaces` y `dominio/puertos` (o al menos renombrar
   `interfaces` a algo con intención, p. ej. `dominio/modelo`).
4. Limpiar el índice de git (`git add -A`) para que el *rename* de `IPagable`
   quede registrado como un solo movimiento.

---

## 4. SOLID

### SRP — Responsabilidad única · **Bien, con asimetrías**

- Cada caso de uso tiene una sola razón de cambio. ✔
- La fachada **solo delega**, no decide (comentario del propio archivo cumplido). ✔
- **Asimetría:** el parseo de líneas de `productos` está encapsulado en factories
  (`IPagableFactory`), pero el de `clientes` y `usuarios` está *inline* con
  `split(";")` dentro de `CargarClientes`/`CargarUsuarios`
  (`CargarClientes.java:30-35`). Mezcla "orquestar carga" + "parsear formato".
  *Sugerencia:* extraer `ClienteFactory` / `UsuarioFactory` simétricas, o al
  menos un método privado `parsear(linea)`.

### OCP — Abierto/cerrado · **Excelente para pagables, débil en UI y notificador**

- **Pagables:** añadir un tipo nuevo = nueva `IPagableFactory` + 1 línea en
  `RegistradorFabricasIPagable`. `SER` (servicios) se añadió exactamente así, sin
  tocar la lógica de los tipos existentes. ✔ Muy buen uso de OCP.
- **`MenuFarmacia` NO es cerrado:** para mostrar un `IPagable` con atributos
  propios hay que añadir ramas `instanceof` (`MenuFarmacia.java:120`, `:144`).
- **`INotificador` NO es cerrado:** un tipo de aviso nuevo obliga a tocar la
  interfaz y **todos** sus implementadores.

### LSP — Sustitución de Liskov · **Punto más débil**

- `RegistrarVenta` castea `IPagable`→`Producto` (§2.2): `Servicio` **no** es
  sustituible.
- `IRepositorioPagable.obtenerTodos()` promete `List<IPagable>` pero
  `VerificarAlertas` (`.java:21-24`, `:35-38`) y `MenuFarmacia` asumen `Producto`.
- `Venta` ya acepta `IPagable` (bien), pero el resto de la cadena de venta no.

### ISP — Segregación de interfaces · **Mixto**

- `IPagable` (2 métodos, `getPrecio`/`getNombre`) está bien segregada… quizá
  *demasiado*: no cubre lo que la presentación necesita, y de ahí los `instanceof`.
- `INotificador` agrupa 4 avisos no relacionados (`stockMinimo`, `vencimiento`,
  `puntosAcumulados`, `ventaRegistrada`). Un caso de uso que solo notifica ventas
  depende de firmas de stock/vencimiento/puntos.
  *Sugerencia:* segregar (`INotificadorInventario`, `INotificadorFidelizacion`,
  `INotificadorVentas`) y que `NotificadorConsola` implemente las tres. La salida
  no cambia.

### DIP — Inversión de dependencias · **Muy bien**

- Los puertos viven en `dominio/puertos`; los adaptadores en `infraestructura`;
  los casos de uso dependen de interfaces; la única clase que conoce
  implementaciones concretas es `AplicacionFarmaciaBuilder`. ✔
- `AutenticacionPorCredenciales` depende de `IRepositorioUsuario`, no de la lista. ✔
- Detalle menor: `Main` depende de las clases concretas `EnsambladorAplicacion` y
  `AplicacionFarmaciaBuilder` — aceptable en un *composition root*.

---

## 5. Patrones de diseño

### 5.1 Inventario y evaluación

| Patrón | Implementación | Veredicto |
|---|---|---|
| **Facade** | `ui/facade/AplicacionFarmacia` | ✔ Correcto. Solo delega; ninguna regla de negocio filtrada. El nombre no dice "Facade" y su ubicación (§3) es discutible. |
| **Builder + Director** | `IAplicacionFarmaciaBuilder`, `AplicacionFarmaciaBuilder`, `EnsambladorAplicacion` | ⚠ Estructuralmente correcto, pero **forzado**: un solo ConcreteBuilder, sin variantes de producto; `comenzar()` resetea campos a `null` que nunca se reutilizan (no se llama dos veces). En la práctica es un *Assembler* / composition root. Vale como ejercicio; su valor real es "un único lugar que conoce implementaciones concretas". |
| **Factory Method** | `IPagableFactory` (abstracta) + `ComestibleFactory`, `CosmeticoFactory`, `MedicamentoCapsulaFactory`, `MedicamentoLiquidoFactory`, `MedicamentoCapsulaFormatoAntiguoFactory`, `ServicioFactory` | ✔ Correcto. Cada ConcreteCreator sabe parsear *su* formato. Añadir un tipo no toca los demás. |
| **Registry / Dispatcher** (a veces llamado *Simple Factory*) | `RegistradorFabricasIPagable` | ✔ Buen mecanismo para OCP: `Map<discriminador, IPagableFactory>` + *fallback* al formato antiguo (compatibilidad AS-IS). Implementa el puerto `IFabricaPagable`. |
| **Flyweight** | `LaboratorioFlyweightFactory` | ✔ Correcto. Comparte `Laboratorio` (inmutable, estado intrínseco) por nombre normalizado. Detalle: si dos líneas declaran el mismo laboratorio con distinta dirección, gana la primera — irrelevante aquí porque dirección/teléfono son constantes por defecto. |
| **Ports & Adapters (Hexagonal)** | `dominio/puertos/*` + `infraestructura/.../adaptadores/*` | ✔ Bien aplicado. (Los "Adaptador…" implementan el puerto; no son *Adapter* GoF en sentido estricto, pero el nombre es correcto en el vocabulario hexagonal.) |
| **Repository** | `IRepositorio*` + `Adaptador…Memoria` | ✔ Estándar, almacenamiento en memoria. |
| **Strategy** (implícito) | `IAutenticacion` / `AutenticacionPorCredenciales` | ✔ Permite cambiar el mecanismo de login sin tocar el caso de uso. |

### 5.2 Patrón ausente que resolvería F1/F2/F4

Los `instanceof Producto` + *downcast* en `MenuFarmacia` (2 sitios),
`VerificarAlertas` (2 sitios) y `RegistrarVenta` (1 sitio) son el síntoma de una
abstracción anémica. Dos salidas clásicas:

- **Polimorfismo enriquecido** en `IPagable` (métodos que cada tipo implementa).
- **Visitor** (`IPagableVisitor` con `visitarProducto` / `visitarServicio`) si se
  quiere mantener `IPagable` mínima y agrupar fuera las operaciones que varían
  (render de línea, alertas, efecto de venta).

### 5.3 Sugerencia concreta: quitar el `cast` de la venta (F2)

Opción A — polimorfismo en la interfaz (mínimo cambio, sin tocar salida):

```java
// dominio/interfaces/IPagable.java
public interface IPagable {
    BigDecimal getPrecio();
    String getNombre();
    /** Efecto de vender 'cantidad' unidades sobre el inventario. */
    default void registrarSalida(int cantidad) { /* servicios: no-op */ }
}
```
```java
// dominio/catalogo/Producto.java
@Override
public void registrarSalida(int cantidad) { descontarStock(cantidad); }
```
```java
// aplicacion/casosdeuso/RegistrarVenta.java  (sin cast)
IPagable aVender = productoEncontrado.get();
aVender.registrarSalida(cantidadVendida);
Venta venta = new Venta(LocalDateTime.now(), cantidadVendida, aVender);
```

Opción B — Visitor, si se prefiere no ensanchar `IPagable`.

Ambas dejan la salida por consola **idéntica** (para productos el efecto es el
mismo `descontarStock`).

### 5.4 Sugerencia concreta: no mezclar servicios en "Ver productos" (F3)

Hoy `ListarPagables.ejecutar()` y `BuscarPagable.ejecutar()` devuelven **todo**
el repositorio. Cuando entren líneas `SER;`, la opción 1 del menú listará
servicios entre los productos y la opción 3 podrá devolver un servicio → la
salida cambiaría respecto al AS-IS.

Alternativas (elegir una y documentarla):

1. **Filtrar en el caso de uso de listado de productos**: `ListarPagables`
   devuelve solo `Producto` para esa vista; un futuro `ListarServicios` para su
   propia vista. Mantiene la salida actual sin cambios.
2. **Repositorios separados** (`IRepositorioProducto` + `IRepositorioServicio`),
   ambos alimentados por el mismo `CargarPagables`. `Venta` sigue aceptando
   `IPagable`. Es la opción más limpia respecto a SRP/LSP, pero más código.
3. Repositorio único + método `obtenerProductos()` / `obtenerServicios()`.

Recomendación: **(1)** ahora (barato, cero cambio de salida) y evolucionar a
**(2)** cuando se añada una vista de servicios.

---

## 6. Lógica que promueve el trato de objetos `IPagable` (Payable)

### 6.1 Qué se hizo

- `IPagable { getPrecio(); getNombre(); }` como mínimo común de `Producto` y `Servicio`.
- `Producto` (abstracta) `implements IPagable`; `Servicio` `implements IPagable`.
- `Venta` referencia `IPagable` (antes `Producto`) → una venta puede ser de
  producto **o** de servicio. ✔ (buen cambio de raíz)
- `IRepositorioPagable`, `IFabricaPagable` y las factories trabajan con `IPagable`.
- Listar/buscar del catálogo ya son polimórficos a nivel de firma.

### 6.2 Qué está incoherente

| Problema | Evidencia | Efecto |
|---|---|---|
| `IPagable` demasiado delgada | `MenuFarmacia.java:120,144`, `VerificarAlertas.java:22,36` | *downcasts* y `instanceof` reintroducen el acoplamiento que la interfaz pretendía eliminar. |
| Cadena de venta no polimórfica | `RegistrarVenta.java:36` | `Servicio` no vendible (ClassCastException). |
| Nombres "Producto" para cosas "Pagable" | `AplicacionFarmacia.java:35-37,88-98`; campos `casoUsoListarProductos:ListarPagables` | El lector no sabe si "producto" incluye servicios. |
| Un repositorio mezcla ambos conceptos | `AdaptadorRepositorioIPagableMemoria` | "Ver productos" mostrará servicios (§5.4). |
| `Producto.getNombre()` / `Servicio.getNombre()` sin `@Override` | ambas clases | menor; oculta que cumplen el contrato de `IPagable`. |

### 6.3 Decisión de fondo pendiente: **¿qué contrato tiene `IPagable`?**

- **Vía mínima + Visitor:** `IPagable` se queda con `getPrecio`/`getNombre`; todo
  lo que varía (línea de listado, alertas, efecto de venta) se modela con un
  Visitor. Ventaja: la interfaz no crece. Coste: una clase Visitor por operación.
- **Vía enriquecida:** `IPagable` gana lo estrictamente necesario para las
  operaciones actuales:
  - `void registrarSalida(int cantidad)` (venta; §5.3),
  - opcionalmente `String lineaListado()` para eliminar el `instanceof` de
    `MenuFarmacia` (aunque esto acerca formato de presentación al dominio — con
    Visitor se evita).

Recomendación pragmática dada la restricción de salida:
`registrarSalida()` polimórfico + filtrado de servicios en la vista de productos
(§5.4 opción 1). Es el conjunto mínimo que hace *coherente* el trato de
`IPagable` sin alterar una sola línea de salida.

---

## 7. Ingreso de servicios que la farmacia comenzará a implementar

### 7.1 Estado actual

- Formato: `SER;nombre;precio;descripcion` (deducido de `ServicioFactory.java:11-14`).
- Ruta: se registra `SER` en `RegistradorFabricasIPagable.java:39` y se carga por
  el **mismo** archivo `datos/productos.txt` a través de `CargarPagables`.
- `datos/productos.txt` **no** contiene líneas `SER;` hoy → la salida por
  terminal es idéntica al AS-IS (verificado).

### 7.2 Problemas / decisiones

1. **`ServicioFactory` no valida longitud.**
   `SER;Consulta;20000` (sin descripción) → `ArrayIndexOutOfBoundsException`.
   Se captura como `RuntimeException` en `CargarPagables` y degrada a
   `"Error al cargar productos"` (mensaje poco útil).
   *Sugerencia:* validar `if (campos.length < 4) throw new IllegalArgumentException("Formato SER invalido: SER;nombre;precio;descripcion")`. Idealmente
   homogeneizar esa validación en todas las factories.

2. **Documentación del formato.** `RegistradorFabricasIPagable` enumera en su
   Javadoc los formatos `MED_CAP`, `MED_LIQ`, `COS`, `COM` pero **no `SER`**.
   Añadir la línea `SER;nombre;precio;descripcion` al comentario.

3. **¿Archivo propio `datos/servicios.txt`?**
   - A favor: separación conceptual, simetría con clientes/usuarios.
   - En contra: obliga a una llamada nueva en `MenuFarmacia.ejecutar()` que
     imprimiría una línea extra ("Servicios cargados") → **cambia la salida**.
   - **Recomendación:** mantener los servicios en `productos.txt` mientras la
     restricción de "salida idéntica" esté vigente; dejar `CargarServicios` +
     `servicios.txt` como evolución posterior planificada.

4. **Venta de servicios:** hoy imposible por §2.2/§5.3. Es el primer arreglo a
   hacer si "implementar servicios" implica poder venderlos.

5. **Listado/búsqueda:** decidir §5.4 para que los servicios no contaminen la
   vista de productos.

6. **Alertas:** `VerificarAlertas` ya los ignora vía `instanceof Producto`
   (`.java:22,36`). Correcto (un servicio no tiene stock ni vencimiento), pero
   conviene dejarlo explícito con un comentario o resolverlo con el mismo Visitor.

### 7.3 Checklist mínimo para habilitar servicios **sin cambiar la salida**

| Paso | Archivo | Cambio sugerido | ¿Afecta salida? |
|---|---|---|---|
| 1 | `IPagable` + `Producto` | `registrarSalida(int)` polimórfico (§5.3) | No |
| 2 | `RegistrarVenta` | quitar el `cast`, usar `registrarSalida` | No (mismo efecto en productos) |
| 3 | `ListarPagables` (vista productos) | filtrar a `Producto` (§5.4 opción 1) | No (hoy no hay servicios) |
| 4 | `BuscarPagable` usado por venta | permitir servicios; el usado por "Buscar producto" del menú, filtrar | No |
| 5 | `ServicioFactory` | validar `campos.length` | No |
| 6 | `RegistradorFabricasIPagable` (Javadoc) | documentar `SER;` | No |
| 7 | `MenuFarmacia.listarProductos()` | **corregir F1** (regresión ya presente) | **Sí: restaura la salida original** |

---

## 8. Resumen ejecutivo

| Dimensión | Estado |
|---|---|
| **Flujo de ejecución** | Correcto y completo de arranque a menú; composición limpia con Builder/Director/Facade. Una **regresión de salida** (F1) y un **bug latente de venta** (F2). |
| **SOLID** | DIP y OCP (para pagables) muy bien. **LSP es el punto débil** (cast a `Producto`). ISP mejorable en `INotificador`. SRP con asimetría en el parseo. |
| **Patrones** | Factory Method + Registry + Flyweight + Facade + Ports&Adapters bien implementados. Builder/Director algo forzado (un solo builder). Falta un mecanismo (polimorfismo/Visitor) para eliminar los `instanceof`. |
| **`IPagable` / Payable** | Buena idea de raíz (`Venta` ya es polimórfica); implementación **incoherente**: interfaz anémica + downcasts + nombres "Producto". Falta definir su contrato. |
| **Ingreso de servicios** | Mecánica de carga (discriminador `SER`) correcta y extensible; **venta de servicios rota**; falta validación y documentación; mantener en `productos.txt` para no alterar la salida. |

### Orden de corrección recomendado (todo sugerido, nada aplicado)

1. **F1** — restaurar el formato de `listarProductos()` (obligatorio por la restricción).
2. **F2/§5.3** — `registrarSalida()` polimórfico; quitar el `cast` de `RegistrarVenta`.
3. **§5.4** — filtrar servicios de la vista "Ver productos"/"Buscar producto".
4. **F5** — validar longitud en `ServicioFactory` (y homogéneo en las demás).
5. **F6/F7/F8** — limpieza: imports muertos, nombres `...Pagable`, índice de git.
6. **§3** — reubicar `AplicacionFarmacia` y `construccion/*` fuera de `ui/`.
7. **ISP** — segregar `INotificador` (opcional, sin impacto en salida).

---

## 9. Correcciones aplicadas (2026-09-06)

Se aplicaron **las 7 correcciones** en el orden recomendado. **La UI (`MenuFarmacia`)
conserva su comportamiento observable**: cada prompt y cada línea de salida de los
flujos que ya existían son idénticos al commit `82cf142` (verificado byte a byte).
Lo único que cambió en `MenuFarmacia` son tipos internos y una ruta de import
(no observable); `listarProductos()` volvió **literalmente** a su versión original.

### 9.1 Paso 1 — F1: formato de `listarProductos()`

`MenuFarmacia.listarProductos()` volvió a iterar `List<Producto>` e imprimir
`nombre\t\tstock\tprecio` en una sola línea (idéntico al AS-IS). El caso de uso
`ListarPagables` se renombró a **`ListarProductos`** y devuelve `List<Producto>`:
proyecta el catálogo a solo-productos con `VisitanteComoProducto` (sin `instanceof`).
Los servicios viven en el mismo repositorio pero **no aparecen en "Ver productos"**.

### 9.2 Paso 2 — F2/§5.3: patrón Visitor para la venta

- **`IPagable`** ahora expone `<R> R aceptar(IPagableVisitor<R> visitante)`.
- **`IPagableVisitor<R>`** (nuevo, en `dominio/interfaces`) con `visitarProducto` /
  `visitarServicio`. Toda la jerarquía `Producto` (Medicamento, Comestible, …) se
  despacha por `visitarProducto`; el eje de variación es "bien con inventario" vs
  "servicio".
- **`VisitanteAfectacionVenta`** (`aplicacion/visitantes`): `visitarProducto` →
  `descontarStock(cantidad)` (misma semántica AS-IS, incluido stock negativo y el
  throw con cantidad ≤ 0); `visitarServicio` → no-op.
- **`RegistrarVenta`** ya no hace `(Producto) ...`: obtiene el `IPagable`, llama
  `aceptar(new VisitanteAfectacionVenta(cantidad))`, registra la `Venta` (que ya
  aceptaba `IPagable`) y notifica. **Vender un servicio dejó de lanzar
  `ClassCastException`.**

### 9.3 Paso 2 (cont.) — Visitor para alertas

- **`VisitanteAlertaStock`** y **`VisitanteAlertaVencimiento`** encapsulan el
  chequeo + notificación; `visitarServicio` no hace nada.
- **`VerificarAlertas`** perdió los `filter(x -> x instanceof Producto)`: ahora
  recorre `obtenerTodos()` y hace `pagable.aceptar(visitante)`. El orden de las
  alertas (todo stock, luego todo vencimiento, en orden de archivo) es idéntico.

### 9.4 Paso 3 — §5.4: servicios fuera de "Ver productos"

Resuelto en `ListarProductos` (§9.1) vía `VisitanteComoProducto`. **Búsqueda y
venta sí resuelven servicios** (decisión acordada): `AplicacionFarmacia.buscarProducto(...)`
devuelve `Optional<IPagable>` y lo usan tanto "Buscar producto" como el precheck de
"Registrar venta", de modo que un servicio es vendible y consultable desde el menú
sin alterar la salida de los productos.

### 9.5 Paso 4 — F5: validación de formato en las factories

`IPagableFactory` incorpora `exigirCampos(campos, minimo, formatoEsperado)`; lo
invocan `ServicioFactory` (`SER;nombre;precio;descripcion`, mín. 4),
`ComestibleFactory`, `CosmeticoFactory`, `MedicamentoCapsulaFactory`,
`MedicamentoLiquidoFactory` y `MedicamentoCapsulaFormatoAntiguoFactory`. Una línea
incompleta ahora falla con `IllegalArgumentException` y mensaje del formato esperado
en vez de un `ArrayIndexOutOfBoundsException` opaco (ambos los captura `CargarPagables`).

### 9.6 Paso 5 — F6/F7/F8: limpieza

- **Imports muertos**: fuera `import ...catalogo.Producto` de `IRepositorioPagable`,
  `IPagableFactory` y `RegistrarVenta`.
- **Nombres**: `ListarPagables` → `ListarProductos`. Los métodos públicos de la
  fachada que la UI consume (`cargarProductos`, `listarProductos`, `buscarProducto`,
  …) se **mantienen** para no tocar `MenuFarmacia`; `buscarProducto` documenta que
  resuelve cualquier `IPagable`.
- **`@Override`** añadido en `Producto.getNombre()` y `Servicio.getNombre()`.
- **Javadoc** de `RegistradorFabricasIPagable` documenta el formato `SER;...`.

### 9.7 Paso 6 — §3: sacar la fachada y el ensamblado de `ui/`

- `farmacia.ui.facade.AplicacionFarmacia` → **`farmacia.aplicacion.fachada.AplicacionFarmacia`**.
- `farmacia.ui.construccion.*` (Builder, Director, interfaz) →
  **`farmacia.aplicacion.construccion.*`**.
- `Main` y `MenuFarmacia` vuelven a importar exactamente las rutas del AS-IS.
  `ui/` queda solo con `MenuFarmacia`.

### 9.8 Paso 7 — ISP: `INotificador` segregado

`INotificador` (4 métodos no relacionados) → **`INotificadorInventario`**
(`stockMinimo`, `vencimiento`), **`INotificadorFidelizacion`** (`puntosAcumulados`),
**`INotificadorVentas`** (`ventaRegistrada`). `NotificadorConsola` implementa las
tres (mensajes/colores intactos). Cada caso de uso depende solo de la que usa:
`VerificarAlertas` → inventario, `AcumularPuntos` → fidelización, `RegistrarVenta`
→ ventas. El builder (composition root) inyecta el `NotificadorConsola` concreto.

### 9.9 Datos: `productos.txt` → `pagables.txt` + semilla de servicios

- Renombrado `datos/productos.txt` → **`datos/pagables.txt`** (`Main` actualizado).
  El mensaje de carga sigue siendo `"Productos cargados"` (salida intacta).
- Las **13 líneas de productos originales se conservan tal cual** (mismo orden y
  valores) → "Ver productos" y las alertas no cambian.
- Añadidas 4 líneas de servicio al final:

  ```
  SER;TomaDePresion;3000;Control de presion arterial
  SER;AplicacionInyeccion;5000;Aplicacion de inyeccion intramuscular
  SER;ControlGlucosa;4000;Medicion de glucosa capilar
  SER;AsesoriaFarmaceutica;8000;Asesoria farmaceutica personalizada
  ```

  Nombres elegidos para no ser subcadena de ningún producto (no alteran búsquedas
  existentes). Prueba: menú 4 → `AplicacionInyeccion` → cantidad `1` → **"Venta
  registrada"** (sin excepción; el servicio no descuenta stock). Menú 3 →
  `ControlGlucosa` → muestra Producto/Precio sin línea "Stock:".

### 9.10 Archivos nuevos / movidos / eliminados

| Acción | Archivo |
|---|---|
| Nuevo | `dominio/interfaces/IPagableVisitor.java` |
| Nuevo | `dominio/puertos/INotificadorInventario.java`, `INotificadorFidelizacion.java`, `INotificadorVentas.java` |
| Nuevo | `aplicacion/visitantes/VisitanteAfectacionVenta.java`, `VisitanteAlertaStock.java`, `VisitanteAlertaVencimiento.java`, `VisitanteComoProducto.java` |
| Eliminado | `dominio/puertos/INotificador.java` |
| Renombrado | `casosdeuso/ListarPagables.java` → `ListarProductos.java` |
| Movido | `ui/facade/AplicacionFarmacia.java` → `aplicacion/fachada/` |
| Movido | `ui/construccion/*` → `aplicacion/construccion/` |
| Renombrado | `datos/productos.txt` → `datos/pagables.txt` |
| Modificado | `IPagable`, `Producto`, `Servicio`, `RegistrarVenta`, `VerificarAlertas`, `AcumularPuntos`, `IRepositorioPagable`, `IPagableFactory` + 6 factories, `RegistradorFabricasIPagable`, `NotificadorConsola`, `AplicacionFarmacia`, `AplicacionFarmaciaBuilder`, `Main`, `MenuFarmacia` |

### 9.11 Lo que **no** se hizo (y por qué)

- **Repositorios separados producto/servicio** (§5.4 opción 2): no hacía falta para
  la restricción; el filtrado por Visitor en `ListarProductos` basta hoy. Queda como
  evolución si aparece una vista "Ver servicios".
- **Renombrar métodos públicos de la fachada** (`buscarProducto` → `buscarPagable`):
  habría obligado a tocar `MenuFarmacia`. Se priorizó no alterar la UI.
- **`descontarStock` que impida stock negativo**: es semántica AS-IS declarada;
  cambiarla alteraría resultados observables.
