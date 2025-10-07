package com.cofincafe.clientes_api.Service;

import com.cofincafe.clientes_api.Model.ClienteCSV;
import com.cofincafe.clientes_api.Model.ClienteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.*;

@Service
public class ClienteService {

    private final String BASE_URL = "https://localhost:8443/fineract-provider";

    @Autowired
    private RestTemplate restTemplate; // Se inyecta desde la config

    /**
     * Obtiene la lista de clientes desde la API de Fineract.
     * 
     * @return lista de clientes DTO
     */
    public List<ClienteDTO> obtenerClientesDesdeApi() {
        try {
            String CLIENTES_URL = BASE_URL + "/api/v1/clients?limit=50&offset=0";

            // Configurar headers obligatorios
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Fineract-Platform-TenantId", "default");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    CLIENTES_URL,
                    HttpMethod.GET,
                    entity,
                    String.class);

            // Leer JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.path("pageItems");

            List<ClienteDTO> clientes = new ArrayList<>();
            for (JsonNode node : items) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setId(node.path("id").asLong());
                cliente.setFirstname(node.path("displayName").asText());
                cliente.setAccountBalance(node.path("accountBalance").asDouble(0.0));
                clientes.add(cliente);
            }

            return clientes;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Consulta el estado de salud del servicio Fineract.
     * 
     * @return String con el estado de salud o mensaje de error
     */
    public String validarSalud() {
        try {
            String HEALTH_URL = BASE_URL + "/actuator/health";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(HEALTH_URL, String.class);

            // Llamada al endpoint de salud
            return responseEntity.getBody();
        } catch (Exception e) {
            // Devuelve el mensaje de error si algo falla
            return "Error al consultar la salud: " + e.getMessage();
        }
    }

    /**
     * Filtra los clientes con balance negativo.
     */
    public List<ClienteDTO> clientesBalanceNegativo() {
        return obtenerClientesDesdeApi().stream()
                .filter(c -> c.getAccountBalance() < 0)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los 3 clientes con mayor balance.
     */
    public List<ClienteDTO> top3Clientes() {
        return obtenerClientesDesdeApi().stream()
                .sorted((c1, c2) -> c2.getAccountBalance().compareTo(c1.getAccountBalance()))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un cliente por ID desde la API en formato DTO.
     */
    public ClienteDTO obtenerClientePorId1(Long id) {
        try {
            String url = BASE_URL + "/api/v1/clients/" + id;

            // Configurar headers obligatorios
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Fineract-Platform-TenantId", "default");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);

            // Leer JSON y mapear al DTO
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.getBody());

            ClienteDTO cliente = new ClienteDTO();
            cliente.setId(node.path("id").asLong());
            cliente.setFirstname(node.path("displayName").asText());
            cliente.setAccountBalance(node.path("accountBalance").asDouble(0.0));

            return cliente;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Obtiene un cliente por ID en formato JsonNode.
     */
    public JsonNode obtenerClientePorId(Long id) {
        try {
            String url = BASE_URL + "/api/v1/clients/" + id;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Fineract-Platform-TenantId", "default");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);

            // Convertir el string JSON a JsonNode
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea un cliente en Fineract.
     */
    public ClienteDTO crearCliente(ClienteDTO cliente) {
        try {
            String url = BASE_URL + "/api/v1/clients";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Fineract-Platform-TenantId", "default");

            // Fecha de activación como string en el formato que Fineract espera
            String fechaStr = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.ENGLISH));

            String jsonBody = String.format(
                    "{ \"officeId\": %d, \"firstname\": \"%s\", \"lastname\": \"%s\", \"legalFormId\": %d, " +
                            "\"active\": true, \"activationDate\": \"%s\", \"submittedOnDate\": \"%s\", " +
                            "\"dateFormat\": \"dd MMMM yyyy\", \"locale\": \"en\" }",
                    cliente.getOfficeId(),
                    cliente.getFirstname(),
                    cliente.getLastname(),
                    cliente.getLegalFormId(),
                    fechaStr,
                    fechaStr);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Convertir la respuesta JSON a ClienteDTO
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            cliente.setId(jsonNode.path("clientId").asLong());
            System.out.println(cliente);
            return cliente;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ================== CSV ================== */

    /**
     * Procesa un archivo CSV y devuelve clientes válidos.
     */
    public List<ClienteCSV> procesarCSV(MultipartFile file) {
        List<ClienteCSV> clientes = new ArrayList<>();
        Set<Integer> clientesProcesados = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean primeraLinea = true;

            while ((line = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = line.split(",");
                if (datos.length < 3)
                    continue; // validar columnas mínimas

                Integer id = validarId(datos[0].trim(), clientesProcesados);
                if (id == null)
                    continue;

                Double balance = validarBalance(datos[2].trim(), datos[1].trim());
                if (balance == null)
                    continue;

                String nombre = datos[1].trim();

                if (validarExistenciaFineract(id, nombre)) {
                    clientes.add(new ClienteCSV(id, nombre, balance));
                    clientesProcesados.add(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientes;
    }

    // Valida que el ID sea número y no esté duplicado
    private Integer validarId(String idStr, Set<Integer> clientesProcesados) {
        try {
            int id = Integer.parseInt(idStr);
            if (clientesProcesados.contains(id))
                return null; // duplicado
            return id;
        } catch (NumberFormatException e) {
            System.out.println("ID inválido: " + idStr);
            return null;
        }
    }

    // Valida que el balance sea número
    private Double validarBalance(String balanceStr, String nombre) {
        try {
            return Double.parseDouble(balanceStr);
        } catch (NumberFormatException e) {
            System.out.println("Balance inválido para cliente " + nombre + ": " + balanceStr);
            return null;
        }
    }

    // Valida existencia del cliente en Fineract
    private boolean validarExistenciaFineract(int id, String nombreCSV) {
        JsonNode cliente = obtenerClientePorId((long) id);

        if (cliente != null
                && cliente.path("id").asInt(-1) == id
                && !cliente.path("displayName").asText("").isEmpty()
                && cliente.path("displayName").asText("").equalsIgnoreCase(nombreCSV)) {
            return true;
        }
        return false;
    }

    /**
     * Filtra los clientes que tienen balance negativo.
     *
     * @param clientes Lista de clientes a filtrar.
     * @return Lista de clientes cuyo balance es menor a 0.
     */
    public List<ClienteCSV> filtrarNegativos(List<ClienteCSV> clientes) {
        return clientes.stream()
                .filter(c -> c.getBalance() < 0)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los 3 clientes con mayor balance de una lista.
     *
     * @param clientes Lista de clientes a ordenar.
     * @return Lista con los 3 clientes con mayor balance.
     */
    public List<ClienteCSV> top3Clientes(List<ClienteCSV> clientes) {
        return clientes.stream()
                .sorted((c1, c2) -> Double.compare(c2.getBalance(), c1.getBalance()))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * Genera un JSON en formato legible (pretty print) a partir de una
     * lista de clientes u objetos.
     *
     * @param clientes Lista de objetos a convertir en JSON.
     * @return Cadena JSON con los datos de los clientes. Devuelve "[]" en caso de
     *         error.
     */
    public String generarJSONClientes(List<?> clientes) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(clientes);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

}
