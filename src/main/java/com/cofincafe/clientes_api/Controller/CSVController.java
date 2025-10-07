package com.cofincafe.clientes_api.Controller;

import com.cofincafe.clientes_api.Model.*;
import com.cofincafe.clientes_api.Service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CSVController {

    private static List<ClienteCSV> clientesGuardados = null;
    @Autowired
    private ClienteService clienteService;

    /**
     * Vista principal con los primeros 10 clientes desde la API
     * URL: /home
     */
    @GetMapping("/home")
    public String verClientes(Model model) {
        try {
            // Intentar obtener los primeros 10 clientes
            List<ClienteDTO> clientes = clienteService.obtenerClientesDesdeApi().stream()
                    .limit(10)
                    .collect(Collectors.toList());

            model.addAttribute("clientes", clientes);

            System.out.println(clienteService.validarSalud());
            return "Home"; // Muestra la vista Home.html

        } catch (Exception e) {
            // Log completo para depuración
            e.printStackTrace();

            // Mensaje de error detallado para el usuario
            String mensajeError = "No se pudieron obtener los clientes.";
            if (e.getCause() != null) {
                mensajeError += " Causa: " + e.getCause().getMessage();
            } else {
                mensajeError += " Detalles: " + e.getMessage();
            }

            model.addAttribute("error", mensajeError);
            return "Error"; // Muestra la vista Error.html
        }
    }

    /**
     * Vista para subir CSV
     * URL: /ClientesTemplate
     */
    @GetMapping("/ClientesTemplate")
    public String ClientesTemplate() {
        return "ClientesTemplate"; // muestra upload.html
    }

    /**
     * Procesar CSV con validaciones, filtrar negativos y top 3
     * URL: /procesar-csv
     */
    @PostMapping("/procesar-csv")
    public String procesarCSV(@RequestParam("file") MultipartFile file, Model model) {

        // 1️⃣ Procesar CSV con el Service
        List<ClienteCSV> clientes = clienteService.procesarCSV(file);
        clientesGuardados = clientes;

        // 2️⃣ Clientes con balance negativo
        List<ClienteCSV> clientesNegativos = clienteService.filtrarNegativos(clientes);

        // 3️⃣ Top 3 clientes con mayor balance
        List<ClienteCSV> top3Clientes = clienteService.top3Clientes(clientes);

        // 4️⃣ Generar JSON
        String clientesJson = clienteService.generarJSONClientes(clientes);

        // 5️⃣ Enviar todo al modelo
        model.addAttribute("clientes", clientes);
        model.addAttribute("clientesNegativos", clientesNegativos);
        model.addAttribute("top3Clientes", top3Clientes);
        model.addAttribute("clientesJson", clientesJson);

        return "tabla"; // Thymeleaf
    }

    /**
     * Descargar clientes procesados como JSON
     * URL: /clientes/json
     */
    @GetMapping("/clientes/json")
    public ResponseEntity<byte[]> descargarJSON() {
        List<ClienteCSV> clientes = clientesGuardados != null ? clientesGuardados : new ArrayList<>();
        String json = clienteService.generarJSONClientes(clientes);
        byte[] jsonBytes = json.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"clientes.json\"")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBytes);
    }

}
