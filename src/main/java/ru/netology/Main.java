package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {
  public static void main(String[] args) {

    Server server = new Server();

      // добавление хендлеров (обработчиков)
      server.addHandler("GET", "/classic.html", new Handler() {
          public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
              // TODO: handlers code
              final var out = responseStream;
              final var filePath = Path.of(".", "public", request.getPathReq());
              final var mimeType = Files.probeContentType(filePath);
              final var template = Files.readString(filePath);
              final var content = template.replace(
                      "{time}",
                      LocalDateTime.now().toString()
              ).getBytes();
              out.write((
                      "HTTP/1.1 200 OK\r\n" +
                              "Content-Type: " + mimeType + "\r\n" +
                              "Content-Length: " + content.length + "\r\n" +
                              "Connection: close\r\n" +
                              "\r\n"
              ).getBytes());
              out.write(content);
              out.flush();
          }
      });

      server.addHandler("GET", "/events.html", new Handler() {
          public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
              // TODO: handlers code
              final var out = responseStream;
              final var filePath = Path.of(".", "public", request.getPathReq());
              final var mimeType = Files.probeContentType(filePath);
              final var length = Files.size(filePath);
              out.write((
                      "HTTP/1.1 200 OK\r\n" +
                              "Content-Type: " + mimeType + "\r\n" +
                              "Content-Length: " + length + "\r\n" +
                              "Connection: close\r\n" +
                              "\r\n"
              ).getBytes());
              Files.copy(filePath, out);
              out.flush();
          }
      });

      server.addHandler("GET", "/events.js", new Handler() {
          public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
              // TODO: handlers code
              final var out = responseStream;
              final var filePath = Path.of(".", "public", request.getPathReq());
              final var mimeType = Files.probeContentType(filePath);
              final var length = Files.size(filePath);
              out.write((
                      "HTTP/1.1 200 OK\r\n" +
                              "Content-Type: " + mimeType + "\r\n" +
                              "Content-Length: " + length + "\r\n" +
                              "Connection: close\r\n" +
                              "\r\n"
              ).getBytes());
              Files.copy(filePath, out);
              out.flush();
          }
      });

    server.listen(9999);

  }
}


