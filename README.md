# AREP Taller 4 💻
## Modularización con Virtualización e Introducción a Docker

Este proyecto es un framework web ligero desarrollado en Java que permite a los desarrolladores crear aplicaciones web con servicios REST y gestionar archivos estáticos (HTML, CSS, JavaScript, imágenes, etc.). El framework proporciona herramientas para definir rutas REST usando funciones lambda, extraer parámetros de consulta de las solicitudes y especificar la ubicación de archivos estáticos. Además, se ha mejorado para manejar solicitudes concurrentes y asegurar un cierre elegante del servidor.

En la aplicación web podrás añadir los componentes que quieres y te hacen falta para armar tu computador deseado. 😎

### Application Screenshots
![image](https://github.com/user-attachments/assets/44185ea0-7236-45b4-936c-231ad36210ec)

![image](https://github.com/user-attachments/assets/eb18e0e2-d62c-4486-b4d6-26d3bbd63cbf)

![image](https://github.com/user-attachments/assets/d8805f6a-3eca-42fe-a60f-978070257f37)

![image](https://github.com/user-attachments/assets/c4445cc5-df94-48f6-88fc-b39446890ab2)

![image](https://github.com/user-attachments/assets/80b049e2-7faf-4578-8339-cabd6ee5eefb)

![image](https://github.com/user-attachments/assets/9692624e-5ec5-471f-90ba-79e016adcac3)

---

### Main features

1. **Definición de Rutas REST**:
    - Permite definir rutas REST usando el método `get()` y `post()`.
    - Soporta el uso de funciones lambda para manejar solicitudes y respuestas.
    - Ejemplo:
        
        ```java
        get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        ```
        
2. **Extracción de Parámetros de Consulta**:
    - Proporciona un mecanismo para extraer parámetros de consulta de las URLs.
    - Ejemplo:
        
        ```java
        get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        ```
        
3. **Gestión de Archivos Estáticos**:
    - Permite especificar la carpeta donde se encuentran los archivos estáticos usando el método `staticfiles()`.
    - Ejemplo:
        
        ```java
        staticfiles("src/main/webapp");
        ```
4. **Concurrencia**:
    - El servidor ahora maneja solicitudes concurrentes utilizando un `ExecutorService` con un pool de hilos.
    - Esto permite que el servidor atienda múltiples solicitudes simultáneamente sin bloquearse.
    - Ejemplo:

        ```java
        private static final int THREADS = 10;
        private static ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
        ```

5. **Cierre Elegante**:
    - El servidor puede detenerse de manera segura, cerrando todos los hilos en ejecución y liberando recursos.
    - Ejemplo:

        ```java
        public static void stop() {
            running = false;
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
            System.out.println("Servidor detenido correctamente.");
        }
        ```
6. **Escalabilidad y Mantenibilidad**:
    - Diseñado para ser ligero y fácil de extender.
    - Ideal para aplicaciones pequeñas y medianas que requieren un servidor HTTP personalizado.

---

### Prerequisites

Para ejecutar este proyecto necesitas instalar lo siguiente:

```
- Java 17 o superior
- Maven 3.8.1 o superior (la versión en el entorno donde fue creado es la 3.9.9)
- Un navegador web
```
En caso de no tener maven instalado, aquí encuentras un tutorial [Maven](https://dev.to/vanessa_corredor/instalar-manualmente-maven-en-windows-10-50pb).


---

### Installing ⚙️

Sigue estos pasos para obtener un entorno de desarrollo funcional:

1. Clona este repositorio:

```
git clone https://github.com/AnaDuranB/Taller-04-AREP.git
```

2. Ingresa al directorio del proyecto:

```
cd Taller-04-AREP
```

En caso de no contar con un IDE de java que se haga responsable de la compilación y ejecución:

3. Compila el proyecto con Maven:

```
mvn clean compile
```

4. Ejecuta el servidor:

```
java -cp target/classes org.example.MicroSpringBoot
```
![img_1.png](imgss/img_1.png)


5. Abre tu navegador y accede a:

```
http://localhost:35000/
```

---

## Project Structure

```
src/
  main/
    java/
      org/
        example/
            annotations/               # Anotaciones personalizadas
                GetMapping.java
                PostMappingjava
                RequestBody.java
                Requestparamjava
                RestControllerjava
            controller/
                ComponentController.java   # Controlador para manejar componentes
                GreetingController.java    # Controlador de ejemplo
            model/
                Component.java        # Modelo de datos para componentes
            server/
                HttpServer.java       # Clase principal del servidor
                Request.java          # Maneja las solicitudes HTTP
                Response.java         # Maneja las respuestas HTTP
          MicroSpringBoot.java        # Clase principal del framework IoC
    webapp/                    # Carpeta de archivos estáticos             
        index.html              # Archivo HTML
        styles.css              # Archivo CSS
        script.js               # Archivo JavaScript
  test/
    java/                       # Pruebas unitarias
pom.xml                         # Archivo de configuración de Maven
README.md                       # Documentación del proyecto
```

---

## Examples of Use

### 1. Definición de Rutas REST

```java
public static void main(String[] args) {
    staticfiles("src/main/webapp");

    get("/hello", (req, res) -> "Hello " + req.getValues("name"));
    get("/pi", (req, res) -> String.valueOf(Math.PI));

    get("/api/components", (req, res) -> {
        return toJson(components);
    });

    post("/api/components", (req, res) -> {
        String body = req.getBody();
        Map<String, String> data = parseJson(body);
        if (data.containsKey("name") && data.containsKey("type") && data.containsKey("price")) {
            components.add(new Component(data.get("name"), data.get("type"), Double.parseDouble(data.get("price"))));
            return "{\"message\": \"Component added successfully\"}";
        }
        return "{\"error\": \"Missing fields\"}";
    });

    start(35000);
}
```

### 2. HTTP requests

- **GET `/hello?name=Pedro`**:
    - Respuesta: `Hello Pedro`
- **GET `/api/components`**:
    - Respuesta: `[{"name": "AMD RYZEN 5 5600X", "type": "CPU", "price": 769999}]`
- **POST `/api/components`** (con cuerpo JSON):
    
    
    ```json
    {
      "name": "AMD RYZEN 5 5600X",
      "type": "CPU",
      "price": 769999
    }
    ```
    
    - Respuesta: `{"message": "Component added successfully"}`

### 3. Static Files

- **GET `/index.html`**:
    - Sirve el archivo `index.html` ubicado en `src/main/webapp`.

---

## Running the tests

El servidor puede probarse usando herramientas como:

- **Navegador web**: Para solicitudes GET y archivos estáticos.
- **Postman**: Para enviar solicitudes POST y probar la API REST.
- **curl**: Para pruebas desde la terminal.

Para ejecutar las pruebas automatizadas:

```
mvn test
```
![img_2.png](imgss/img_2.png)
![img_3.png](imgss/img_3.png)
Estas pruebas verifican la correcta respuesta del servidor ante diferentes solicitudes.


Ejemplo con `curl`:

```bash
curl http://localhost:35000/hello?name=Ana
curl -X POST http://localhost:35000/api/components -d '{"name": "AMD RYZEN 5 5600X", "type": "CPU", "price": 769999}'
```

## Built With

- [Java SE](https://www.oracle.com/java/) - Lenguaje de programación
- [Maven](https://maven.apache.org/) - Herramienta de gestión de dependencias y construcción

## Authors

- Ana Maria Duran - *AREP* *Taller 4* - [AnaDuranB](https://github.com/AnaDuranB)
