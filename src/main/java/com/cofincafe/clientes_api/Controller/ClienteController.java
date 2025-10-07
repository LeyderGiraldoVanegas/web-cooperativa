package com.cofincafe.clientes_api.Controller;

import com.cofincafe.clientes_api.Model.ClienteDTO;
import com.cofincafe.clientes_api.Service.ClienteService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Listar clientes con balance negativo
     * URL: /cliente/negativos
     * Método: GET
     * Retorna: List<ClienteDTO>
     */
    @GetMapping("/negativos")
    public List<ClienteDTO> listarNegativos() {
        return clienteService.clientesBalanceNegativo();
    }

    /**
     * Listar top 3 clientes por balance
     * URL: /cliente/top3
     * Método: GET
     * Retorna: List<ClienteDTO>
     */
    @GetMapping("/top3")
    public List<ClienteDTO> listarTop3() {
        return clienteService.top3Clientes();
    }

    /**
     * Ver detalle de un cliente
     * URL: /cliente/detalle/{id}
     * Método: GET
     * Parámetros: id (PathVariable)
     * Retorna: Vista "clienteDetalle" con clienteJson
     */
    @GetMapping("/detalle/{id}")
    public String verCliente(@PathVariable Long id, Model model) {
        // Obtener JSON directamente desde el servicio
        JsonNode clienteJson = clienteService.obtenerClientePorId(id);

        // Convertir a String para enviarlo al HTML
        String clienteString = clienteJson.toString();

        model.addAttribute("clienteJson", clienteString);
        return "clienteDetalle";
    }

    /**
     * Formulario para crear cliente
     * URL: /cliente/nuevo
     * Método: GET
     * Retorna: Vista "crearCliente"
     */
    @GetMapping("/nuevo")
    public String plantillaCliente() {
        return "crearCliente"; // HTML sin extensión
    }


    /**
     * Crear un cliente
     * URL: /cliente/crear
     * Método: POST
     * Parámetros: ClienteDTO desde formulario
     * Redirige:
     *   - /cliente/detalle/{id} si se creó correctamente
     *   - /cliente/crear?error=true si falla
     */
    @PostMapping("/crear")
    public String crearCliente(@ModelAttribute ClienteDTO cliente, Model model) {
        
        ClienteDTO nuevoCliente = clienteService.crearCliente(cliente);
        System.out.println("crear: " + nuevoCliente);
        if (nuevoCliente != null && nuevoCliente.getId() != null) {
            // Redirige al detalle del cliente recién creado
            return "redirect:/cliente/detalle/" + nuevoCliente.getId();
        } else {
            // Si falla, redirige de nuevo al formulario con un parámetro de error
            return "redirect:/cliente/crear?error=true";
        }
    }

}
