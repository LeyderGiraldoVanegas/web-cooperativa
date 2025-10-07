package com.cofincafe.clientes_api.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaMundoController {

    @GetMapping("/hola")
    public String decirHola() {
        return "¡Hola Mundo desde Spring Boot!";
    }
}
