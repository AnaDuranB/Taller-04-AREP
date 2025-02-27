# AREP Taller 4 💻
# Modularización con Virtualización e Introducción a Docker

Este proyecto es un framework web ligero desarrollado en Java que permite a los desarrolladores crear aplicaciones web con servicios REST y gestionar archivos estáticos (HTML, CSS, JavaScript, imágenes, etc.). El framework proporciona herramientas para definir rutas REST usando funciones lambda, extraer parámetros de consulta de las solicitudes y especificar la ubicación de archivos estáticos. Además, se ha mejorado para manejar solicitudes concurrentes y asegurar un cierre elegante del servidor.

En la aplicación web podrás añadir los componentes que quieres y te hacen falta para armar tu computador deseado. 😎


## Demo del Proyecto

<video src="https://github.com/AnaDuranB/Taller-04-AREP/imgss/taller-04-arep.mp4" controls width="100%"></video>

---
## Prerequisites ⚙️

Para ejecutar este proyecto necesitas instalar lo siguiente:

```
- Java 17 o superior
- Maven 3.8.1 o superior (la versión en el entorno donde fue creado es la 3.9.9)
- Un navegador web
```

En caso de no tener maven instalado, aquí encuentras un tutorial [Maven](https://dev.to/vanessa_corredor/instalar-manualmente-maven-en-windows-10-50pb).

---
## Project Architecture

El proyecto sigue una arquitectura cliente-servidor con un backend en Java y un frontend en HTML, CSS y JavaScript.

Componentes principales de tu arquitectura:

1. **Servidor web en Java (MicroSpringBoot)**
    - Maneja las solicitudes HTTP.
    - Usa controladores (@RestController) para definir endpoints.
    - Responde con archivos estáticos o datos JSON.
2. **Sistema de ruteo y anotaciones personalizadas**
    - Implementa una versión ligera de Spring con @GetMapping, @PostMapping, etc.
    - Permite definir endpoints de manera declarativa.
3. **Frontend basado en HTML, CSS y JavaScript**
    - Se comunica con el backend mediante fetch().
    - Carga y muestra los datos de los componentes en la UI.
4. **Despliegue en contenedores Docker**
    - Se generan imágenes Docker para ejecutar la aplicación.
    - Se pueden levantar varias instancias en AWS.

![image](https://github.com/user-attachments/assets/320cf388-81b7-4367-b525-5c44fc0b1394)

---
## Project Structure and Class Design

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

#### 1. **Servidor HTTP (`HttpServer.java`)**

Clase principal que inicia el servidor, escucha las solicitudes y las distribuye a los controladores correspondientes.

#### 2. **Manejador de Solicitudes (`RequestHandler.java`)**

Encargado de analizar las solicitudes HTTP y responder apropiadamente según la ruta.

#### 3. **Manejo de Archivos Estáticos (`StaticFileHandler.java`)**

Sirve archivos HTML, CSS, JS e imágenes desde el directorio configurado.

#### 4. **Gestión de Componentes (`ComponentController.java`)**

Implementa la API REST para agregar y listar componentes de computadoras en memoria.

#### 5. **Modelo de Datos (`Component.java`)**

Representa los datos de un componente (nombre, tipo y precio) y se almacena temporalmente en una lista en memoria.

---

## Build and Deploy with Docker: 🐳

### Generación de Imágenes Docker:

Para construir la imagen del servidor:

```bash
docker build --tag dockertaller4 .
```
![img_1.png](imgss/docker_ss/img_1.png)


### Ejecución de los Contenedores:

1. Para ejecutar los servicios de forma local con Docker:

```bash
docker run -d -p 34000:35000 -e DOCKER_ENV=true --name dockercontainer dockertaller4
```
![img.png](imgss/docker_ss/img_cmd.png)
![img.png](imgss/docker_ss/img.png)

Aplicación corriendo correctamente:

- 34000
![img_2.png](imgss/docker_ss/img_2.png)

- 34001
![img_3.png](imgss/docker_ss/img_3.png)

- 34002
![img_4.png](imgss/docker_ss/img_4.png)


2. Subimos la imagen a DockerHub, a nuestro respectivo repositorio.

![image](https://github.com/user-attachments/assets/02f1b8d1-ec1c-45a1-b041-43e5318b3b61)

![image](https://github.com/user-attachments/assets/7f6b0856-8720-4f05-83d4-a1b5fe1644d9)


## Despliegue en AWS con EC2

1. Crear una instancia EC2.

![instancia](https://github.com/user-attachments/assets/6872cd83-4a60-4f19-bcf3-ba03e64217d6)

2. Configurar el puerto en el grupo de seguridad.

![puertos](https://github.com/user-attachments/assets/e3bf45d4-64b3-4a77-899f-951e5df3cf07)

3. Conectarse vía SSH.

![Imagen de WhatsApp 2025-02-18 a las 15 14 34_3c696889](https://github.com/user-attachments/assets/718fe2e3-6b93-402c-abb3-62473e3260df)

4. Instalar Docker en la instancia:

```bash
sudo yum update -y
sudo yum install docker
sudo service docker start
```

5. Transferir las imágenes a la instancia y ejecutarlas con los mismos comandos anteriores. En este caso:
   
```bash
docker run -d -p 42000:35000 -e DOCKER_ENV=true --name dockercontaineraws 1000380607/taller04arep
```

Abrimos el link del despliegue:

![despliegue](https://github.com/user-attachments/assets/e89bdac3-8009-4a84-bc5f-aaab19d34abd)

![greeting funcionando aws](https://github.com/user-attachments/assets/40443a94-085b-4010-a6af-0a569e6b596a)

---
### Main features

1. **Definición de Rutas REST**:
    - Permite definir rutas REST usando el método `get()` y `post()`.
    - Soporta el uso de funciones lambda para manejar solicitudes y respuestas.
        
2. **Extracción de Parámetros de Consulta**:
    - Proporciona un mecanismo para extraer parámetros de consulta de las URLs.
        
3. **Gestión de Archivos Estáticos**:
    - Permite especificar la carpeta donde se encuentran los archivos estáticos usando el método `staticfiles()`.

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

### Installing ⚙️

Sigue estos pasos si solo quieres usar la aplicación localmente en una sola instancia:

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
![image](https://github.com/user-attachments/assets/d4b5772e-cbb7-445f-93a8-550809f62d73)



5. Abre tu navegador y accede a:

```
http://localhost:35000/
```

---

---

## Running the tests


![image](https://github.com/user-attachments/assets/64649c3d-b080-4714-94a1-44a659137201)


Para ejecutar las pruebas automatizadas:

```
mvn test
```
![image](https://github.com/user-attachments/assets/59e74539-fff2-49b5-943e-42470a951243)

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
