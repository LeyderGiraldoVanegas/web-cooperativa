COOPERATIVA DE AHORRO Y CRÉDITO CAFETERO
FINANCIERA COFINCAFE
Cargo al que aplica: Desarrollador
 Fecha de aplicación de la prueba: 06/10/2025
 Fecha de finalización: 08/10/2025

Datos de Referencia
Proyecto base: Apache Fineract
 Lenguaje: Java 21











Autor:
 Leyder Giraldo Vanegas



COOPERATIVA DE AHORRO Y CRÉDITO CAFETERO	1
FINANCIERA COFINCAFE	1
Datos de Referencia	1
Preguntas Teóricas (Conceptuales, Cortas)	3
1. Diferencia entre HashMap y ConcurrentHashMap	3
2. Diferencia entre List, Set y Map en Java Collections	3
3. ¿Qué es un Optional en Java y para qué sirve?	3
4. ¿Cómo manejarías excepciones en un servicio que accede a base de datos?	3
5. Diferencia entre @Component, @Service y @Repository en Spring	3
6. En Fineract, el backend expone una API REST: ¿cómo mapeamos un endpoint?	4
Ejercicio Práctico Corto	4
Arquitectura y Componentes	4
Modelo (Model)	5
ClienteDTO	5
ClienteCSV	5
Servicio (Service)	5
ClienteService	5
API Fineract	6
Procesamiento CSV	6
Validaciones internas	6
Controladores (Controller)	6
ClienteController	6
CSVController	7
Flujo de Datos	7
Clientes desde API	7
Creación de Clientes	8
Procesamiento de Archivos CSV	10
Documentación de Endpoints	13



Preguntas Teóricas (Conceptuales, Cortas)
Diferencia entre HashMap y ConcurrentHashMap

HashMap: Ideal para entornos monohilo. Es rápido pero no seguro en concurrencia.


ConcurrentHashMap: Diseñado para entornos multihilo, permite acceso concurrente seguro y mantiene la integridad de los datos.


Diferencia entre List, Set y Map en Java Collections

List: Almacena elementos en un orden definido, permite duplicados.


Set: Almacena elementos únicos, sin orden garantizado.


Map: Estructura de pares clave–valor, cada clave es única.


¿Qué es un Optional en Java y para qué sirve?
Optional es una clase contenedora que representa un valor que puede estar presente o vacío. 
Se utiliza para evitar el uso de valores nulos directamente y prevenir el error NullPointerException.
¿Cómo manejarías excepciones en un servicio que accede a base de datos?
Implementaría un bloque try-catch para capturar excepciones específicas, registrarlas mediante logging y lanzar excepciones personalizadas para que la capa del front las maneje de forma controlada.
Diferencia entre @Component, @Service y @Repository en Spring

@Component: Marca una clase para que Spring la gestione automáticamente.


@Service: Indica que la clase contiene la lógica de negocio principal.


@Repository: Indica que la clase gestiona la persistencia de datos (DAO), con manejo automático de excepciones de base de datos.


En Fineract, el backend expone una API REST: ¿cómo mapeamos un endpoint?

@RestController: Define la clase como API REST.


@RequestMapping: Define la ruta base del controlador.


@GetMapping, @PostMapping, etc.: Definen el tipo de petición y la URL del endpoint.


Ejercicio Práctico Corto
Objetivo: Validar nivel medio en Java, colecciones y buenas prácticas.
Enunciado:
 Dado un listado de clientes de una microfinanciera (id, nombre, balance), implementar en Java:
Un método que devuelva los clientes con balance negativo.


Un método que devuelva el Top 3 de clientes con mayor balance.


Un método que convierte esa lista en formato JSON.
Arquitectura y Componentes
Modelo (Model)
ClienteDTO
Representa un cliente dentro de la aplicación con los campos:
Campo
Tipo de Dato
Descripción
id
Long
Identificador único
firstname
String
Nombre del cliente
lastname
String
Apellido del cliente
displayName
String
Nombre completo
officeId
Long
Oficina asignada
legalFormId
Long
Tipo legal
activationDate
LocalDate
Fecha de activación
balance
Double
Saldo actual

Incluye constructores, getters, setters y toString().
ClienteCSV
Representa un cliente importado desde un archivo CSV con los campos:
Campo
Tipo de Dato
Descripción
id
Integer
Identificador
nombre
String
Nombre del cliente
balance
Double
Saldo

 Servicio (Service)
ClienteService
Funcionalidades principales:
API Fineract
obtenerClientesDesdeApi() → Obtiene lista de clientes desde Fineract.


obtenerClientePorId(Long id) → Obtiene un cliente específico.


crearCliente(ClienteDTO cliente) → Crea un cliente en Fineract.


validarSalud() → Verifica el estado del servicio.


Procesamiento CSV
procesarCSV(MultipartFile file) → Procesa un archivo CSV con validaciones.


filtrarNegativos(List<ClienteCSV>) → Devuelve clientes con balance negativo.


top3Clientes(List<ClienteCSV>) → Devuelve los tres clientes con mayor balance.


generarJSONClientes(List<?>) → Genera un archivo JSON de los clientes procesados.


Validaciones internas
Verificación de ID duplicado o inválido


Validación de balance incorrecto o vacío


Comprobación de existencia de cliente en Fineract


Controladores (Controller)
ClienteController






Rutas principales:
Método
URL
Descripción
GET
/cliente/negativos
Lista clientes con balance negativo
GET
/cliente/top3
Lista Top 3 clientes
GET
/cliente/detalle/{id}
Muestra detalle del cliente en HTML
GET
/cliente/nuevo
Formulario para crear cliente
POST
/cliente/crear
Crea un cliente y redirige al detalle


CSVController
Rutas principales:
Método
URL
Descripción
GET
/home
Muestra los primeros 10 clientes
GET
/ClientesTemplate
Formulario para cargar archivo CSV
POST
/procesar-csv1
Carga CSV simple sin validaciones
POST
/procesar-csv
Carga CSV con validaciones y obtiene Top 3
GET
/clientes/json
Descarga la lista procesada como JSON


Flujo de Datos
Clientes desde API
ClienteController llama a ClienteService.obtenerClientesDesdeApi().



Los datos se envían a la vista Thymeleaf (Home.html) para su presentación.


Creación de Clientes
El usuario ingresa en /cliente/nuevo y completa el formulario.



ClienteController.crearCliente() llama a ClienteService.crearCliente().


Se redirige al detalle del cliente /cliente/detalle/{id}.

Procesamiento de Archivos CSV
El usuario sube el archivo CSV en /ClientesTemplate.



CSVController.procesarCSV() invoca ClienteService.procesarCSV().



Se filtran los balances negativos y se obtiene el Top 3.





Los datos se muestran en tabla.html y pueden descargarse como JSON.



Documentación de Endpoints
Método
URL
Descripción
Parámetros
Respuesta
GET
/home
Muestra los primeros 10 clientes
-
Vista HTML con lista
GET
/ClientesTemplate
Formulario para carga de CSV
-
Vista HTML
POST
/procesar-csv1
Procesa CSV simple
file (Multipart)
Vista con clientes
POST
/procesar-csv
Procesa CSV con validaciones
file (Multipart)
Vista con clientes, top3, negativos y JSON
GET
/clientes/json
Descargar clientes procesados como JSON
-
JSON descargable
GET
/cliente/negativos
Listar clientes con balance negativo
-
JSON
GET
/cliente/top3
Listar Top 3 clientes
-
JSON
GET
/cliente/detalle/{id}
Ver detalle del cliente
id
HTML con JSON
GET
/cliente/nuevo
Formulario para creación de cliente
-
HTML
POST
/cliente/crear
Crear cliente nuevo
ClienteDTO
Redirección a detalle
