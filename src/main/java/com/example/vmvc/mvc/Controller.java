package com.example.vmvc.mvc;

import java.io.IOException;

public interface Controller {
    void handle(Request request, Response response) throws IOException;
} 