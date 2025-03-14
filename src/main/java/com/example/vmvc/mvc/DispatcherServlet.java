package com.example.vmvc.mvc;

import com.example.vmvc.handler.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Map<String, Controller> controllers = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(InputStream input, OutputStream output) throws IOException {
        Request request = parseRequest(input);
        Response response = new Response(output);

        try {
            Controller controller = controllers.get(request.getPath());
            if (controller != null) {
                controller.handle(request, response);
            } else {
                send404Response(response);
            }
        } catch (Exception e) {
            logger.error("处理请求失败", e);
            send500Response(response);
        }
    }

    public void registerController(String path, Controller controller) {
        controllers.put(path, controller);
    }

    private Request parseRequest(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        String firstLine = reader.readLine();
        if (firstLine == null) {
            throw new IOException("无效的HTTP请求");
        }

        String[] parts = firstLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("无效的HTTP请求行");
        }

        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int separator = line.indexOf(':');
            if (separator > 0) {
                headers.put(
                    line.substring(0, separator).trim().toLowerCase(),
                    line.substring(separator + 1).trim()
                );
            }
        }

        return new Request(parts[0], parts[1], headers, reader);
    }

    private void send404Response(Response response) throws IOException {
        response.setStatus(404);
        response.setHeader("Content-Type", "application/json");
        response.write(objectMapper.writeValueAsString(Map.of("error", "Not Found")));
    }

    private void send500Response(Response response) throws IOException {
        response.setStatus(500);
        response.setHeader("Content-Type", "application/json");
        response.write(objectMapper.writeValueAsString(Map.of("error", "Internal Server Error")));
    }
} 