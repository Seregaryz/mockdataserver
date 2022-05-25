package ru.itis.kpfu.mockdataserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/begin")
public class TestApiController {

    @GetMapping("/{pathVariable}")
    public ResponseEntity<String> greeting(@PathVariable("pathVariable") String pathVariable) {
        try {
            if (pathVariable.equals("dina")) {
                return ResponseEntity.ok("Kiss kiss");
            } else if (pathVariable.equals("home")) {
                return ResponseEntity.ok("Go home");
            } else return ResponseEntity.ok("unknown request");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }

}
