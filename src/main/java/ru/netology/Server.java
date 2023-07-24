package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    //final List validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
    private ExecutorService service = Executors.newFixedThreadPool(64);
    private Map<String, Map<String, Handler>> handlers = new HashMap<>();


    public Server() {
    }

    public void listen (int port){

                try (final var serverSocket = new ServerSocket(port)) {
                    System.out.println("Server started...");
                    while (true) {

                        final var socket = serverSocket.accept();

                        service.submit(() -> {

                            try {
                                clientResp(socket);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        });

                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
    }

    private void clientResp(Socket socket) throws IOException {
        final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final var out = new BufferedOutputStream(socket.getOutputStream());
        // read only request line for simplicity
        // must be in form GET /path HTTP/1.1
        final var requestLine = in.readLine();
        final var parts = requestLine.split(" ");

        if (parts.length != 3) {
            return;
        }

        Request req = new Request(parts[0], parts[1], parts[2]);

        Handler hr = handlers.get(req.getMethodReq()).get(req.getPathReq());
        if (hr != null){
            hr.handle(req, out);
        } else {
            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Length: 0\r\n" +
                    "Connection: close\r\n" +
                    "\r\n"
            ).getBytes());
            out.flush();
        }
    }

    public void addHandler(String method, String path, Handler handler){
        Map<String, Handler> curHM = handlers.get(method);
        if (curHM == null){
            curHM = new HashMap<String, Handler>();
        }
        curHM.put(path, handler);
        handlers.put(method, curHM);
    }

}
