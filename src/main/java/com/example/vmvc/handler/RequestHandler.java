package com.example.vmvc.handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public interface RequestHandler {
    void handle(InputStream input, OutputStream output) throws IOException;
} 