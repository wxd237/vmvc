package com.example.vmvc.example;

import com.example.vmvc.mvc.Controller;
import com.example.vmvc.mvc.Request;
import com.example.vmvc.mvc.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class HelloController implements Controller {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(Request request, Response response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        
        Map<String, String> responseData = Map.of(
            "message", "Hello from Virtual Thread MVC!",
            "method", request.getMethod(),
            "path", request.getPath()
        );
        
        response.write(objectMapper.writeValueAsString(responseData));
    }
} 