package org.example;

import org.example.server.HttpServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class HttpServerConcurrencyTest {

    @BeforeAll
    static void startServer() {
        new Thread(() -> MicroSpringBoot.main(new String[]{})).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    @AfterAll
    static void stopServer() {
        // Detiene el servidor después de las pruebas
        HttpServer.stop();
    }

    private String sendHttpRequest(String request) throws IOException {
        try (Socket socket = new Socket("localhost", 35000);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            out.write(request.getBytes());
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            int contentLength = 0;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                if (line.isEmpty()) break; // Fin de encabezados
            }

            // Leer el cuerpo de la respuesta
            char[] body = new char[contentLength];
            reader.read(body, 0, contentLength);
            response.append(body);

            return response.toString();
        }
    }


    @Test
    void testGetHello() throws IOException {
        String response = sendHttpRequest("GET /greeting?name=John HTTP/1.1\r\nHost: localhost\r\n\r\n");
        assertTrue(response.contains("200 OK"), "La respuesta no contiene '200 OK'");
        assertTrue(response.contains("Hola, John!"), "La respuesta no contiene 'Hola, John!' en JSON");
    }


    @Test
    void testPostComponent() throws IOException {
        String requestBody = "{\"name\": \"CPU\", \"type\": \"Processor\", \"price\": \"299.99\"}";
        String request = "POST /api/components HTTP/1.1\r\n" +
                "Host: localhost\r\n" +
                "Content-Type: application/json\r\n" +
                "Content-Length: " + requestBody.length() + "\r\n\r\n" +
                requestBody;

        String response = sendHttpRequest(request);
        assertTrue(response.contains("201 Created"));
        assertTrue(response.contains("{message=Component added successfully}"));
    }

    @Test
    void testInvalidRoute() throws IOException {
        String response = sendHttpRequest("GET /invalid HTTP/1.1\r\nHost: localhost\r\n\r\n");
        assertTrue(response.contains("404 Not Found"));
    }

    @Test
    void testConcurrentRequests() throws InterruptedException {
        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];

        // Lanzamos múltiples hilos para hacer peticiones concurrentes
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    String response = sendHttpRequest("GET /greeting?name=Test" + finalI + " HTTP/1.1\r\nHost: localhost\r\n\r\n");

                    System.out.println("Respuesta recibida: " + response + "FINNN");
                    assertTrue(response.contains("200 OK"));
                    assertTrue(response.contains("Hola, Test" + finalI + "!"));
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("Request failed");
                }
            });
            threads[i].start();
        }

        // Espera a que todos los hilos terminen
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    void testPostComponentConcurrent() throws InterruptedException {
        int numThreads = 5;
        Thread[] threads = new Thread[numThreads];

        // Lanzamos múltiples hilos para hacer peticiones concurrentes a la API de componentes
        for (int i = 0; i < numThreads; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    String requestBody = "{\"name\": \"CPU" + finalI + "\", \"type\": \"Processor\", \"price\": \"199.99\"}";
                    String request = "POST /api/components HTTP/1.1\r\n" +
                            "Host: localhost\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: " + requestBody.length() + "\r\n\r\n" +
                            requestBody;

                    String response = sendHttpRequest(request);
                    System.out.println("Respuesta recibida: " + response + "FINNN");
                    assertTrue(response.contains("201 Created"));
                    assertTrue(response.contains("{message=Component added successfully}"));
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("Request failed");
                }
            });
            threads[i].start();
        }

        // Espera a que todos los hilos terminen
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
