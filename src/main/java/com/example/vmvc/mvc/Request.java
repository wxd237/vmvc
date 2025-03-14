package com.example.vmvc.mvc;

import java.io.BufferedReader;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final BufferedReader reader;

    public Request(String method, String path, Map<String, String> headers, BufferedReader reader) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.reader = reader;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public String getHeader(String name) {
        return headers.get(name.toLowerCase());
    }
} 