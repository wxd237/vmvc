package com.example.vmvc.mvc;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private final OutputStream output;
    private final Map<String, String> headers = new HashMap<>();
    private int status = 200;
    private boolean headersSent = false;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void write(String content) throws IOException {
        if (!headersSent) {
            writeHeaders();
        }
        output.write(content.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }

    private void writeHeaders() throws IOException {
        StringBuilder headerString = new StringBuilder();
        headerString.append("HTTP/1.1 ").append(status).append(" ").append(getStatusText(status)).append("\r\n");
        
        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerString.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        
        headerString.append("\r\n");
        output.write(headerString.toString().getBytes(StandardCharsets.UTF_8));
        headersSent = true;
    }

    private String getStatusText(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
} 