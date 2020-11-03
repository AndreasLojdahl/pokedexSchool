package com.example.pokedex.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleNotFound(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getReason());
    }
}
