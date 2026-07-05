# 📊 Servicio Analíticas - Kapitán Kernel

> **Motor de Monitorización y Registro de Tráfico en Tiempo Real**

## 📖 Descripción del Módulo

El **Servicio de Analíticas** actúa como el motor central de monitorización y registro de tráfico en tiempo real de la plataforma "Kapitán Kernel". Su propósito fundamental es procesar, almacenar de forma eficiente y exponer las métricas de visitantes, clics y engagement, generando los datos analíticos clave necesarios para alimentar el dashboard principal del sistema.

---

## 🛠️ Stack y Dependencias

Basado en el entorno de Spring Boot (v4.0.6) y Java 21, este módulo integra herramientas orientadas a rendimiento y seguridad. Las tecnologías clave extraídas del `pom.xml` incluyen:

- **Core**: Spring Boot Web.
- **Persistencia**: Spring Data JPA con conector **MySQL** (`mysql-connector-j`).
- **Seguridad e Identidad**: Spring Boot Security y JJWT (JSON Web Token) para la validación de peticiones.
- **Productividad**: Lombok y Spring Boot DevTools.
- **Validación**: Spring Boot Validation.
- **Testing**: Testcontainers (MySQL), JUnit Jupiter y herramientas nativas de Spring Test.

---

## 🏗️ Arquitectura Orientada a Métricas

> [!IMPORTANT]
> **Aislamiento Estratégico:**  
> Este servicio ha sido diseñado con una arquitectura **completamente desacoplada**. La razón de este aislamiento es asegurar que la ingesta masiva de datos de tráfico (como eventos de tráfico, page views o clics) funcione de manera independiente y asíncrona, garantizando que **no penalice en absoluto** el rendimiento del catálogo de la tienda ni la velocidad de lectura del blog.

---

## 🗺️ Mapa de Endpoints / Rutas

A continuación se detalla la estructura de acceso definida en los controladores (`AnaliticaQueryControlador`, `TrackerController` y `ApiController`):

| Método | Ruta | Propósito | Controlador |
| :---: | :--- | :--- | :--- |
| `GET` | `/api/analitica` | Consulta global de KPIs para el dashboard. | `AnaliticaQueryControlador` |
| `GET` | `/api/analitica/visitantes-recientes` | Devuelve el fragmento (JSON/DTO) de los 10 últimos visitantes recientes para el dashboard. | `AnaliticaQueryControlador` |
| `GET` | `/api/analitica/visitantes-filtrados` | Consulta de engagement aplicando filtros avanzados (ciudad, dispositivo, etc). | `AnaliticaQueryControlador` |
| `POST` | `/api/tracker/evento` | **Ingesta de datos**: Registra visitas o eventos en tiempo real capturando payload y User-Agent. | `TrackerController` |
| `GET` | `/api/tracker/trampa-kernel` | Endpoint "Honeypot" que atrapa bots maliciosos sin firma y registra la intrusión. | `TrackerController` |
| `GET` | `/api/ping` / `/api/secure` | Rutas de health check y validación de contexto de seguridad. | `ApiController` |

---

## ⚡ Optimización Avanzada de Consultas

Para gestionar la alta variabilidad en las consultas del panel de analíticas, este módulo implementa técnicas avanzadas de persistencia, evitando la sobrecarga de memoria y optimizando las consultas SQL a nivel de base de datos:

### 🔍 Consultas Dinámicas (Patrón Specification)
Se ha implementado el uso de **`AnaliticaSpecifications`** apoyado en la Criteria API de JPA. Esto permite construir predicados (`Predicate`) de forma totalmente dinámica basándose en los parámetros que el usuario envíe a través del **`VisitanteFiltroDTO`** (ciudad, dispositivo, sistema operativo, etc.). Solo se añaden a la cláusula `WHERE` las condiciones que realmente existen en la petición, manteniendo el código del servicio limpio de lógica condicional SQL compleja.

### 🚀 Proyecciones Optimizadas (@Query)
Para listados de alto rendimiento en métricas de engagement, utilizamos consultas JPQL con la técnica **Catch-All** (`:param IS NULL OR :param = ''...`) definida en `AnaliticaRepository`. Estas consultas proyectan los resultados directamente sobre un DTO ligero (**`ArticuloEngagementDTO`**). Esto evita la "hidratación" de entidades completas (**`EventoAnalitica`**) por parte de Hibernate, reduciendo drásticamente el consumo de memoria RAM y mejorando exponencialmente los tiempos de respuesta.

---

## 🛡️ Estrategia de Tests (WebMvcTest & Mockito)

El módulo cuenta con una sólida protección en su capa de analíticas y controladores. La estrategia se fundamenta en:

- **Aislamiento de la Capa Web (`@WebMvcTest`)**: Se validan los endpoints y las respuestas HTTP aislándolos del contexto pesado de Spring, asegurando que las validaciones y el enrutamiento sean perfectos.
- **Simulación de Persistencia (Mockito)**: Simulamos el comportamiento de los servicios y la capa de persistencia mediante _Mocks_. Esto nos permite validar que la ingesta de datos a través de los endpoints de tracking funciona "a velocidad de rayo", comprobando el flujo de los objetos JSON **sin tocar la base de datos real**.

*(Adicionalmente, se disponen de tests de integración soportados por **Testcontainers** para validar el modelo de datos en un motor MySQL efímero).*

---

## 🚀 Instrucciones de Ejecución

Para levantar el módulo y ejecutar la suite de pruebas localmente, utiliza los siguientes comandos de Maven ubicándote en la raíz del módulo (`servicio-analiticas`):

### Iniciar el servicio en local:
```bash
mvn spring-boot:run
```

### Ejecutar la suite de pruebas completa:
```bash
mvn clean test
```

### Empaquetar la aplicación omitiendo tests (para despliegue):
```bash
mvn clean package -DskipTests
```
