import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PetStore {
    private static final String API_URL = "https://petstore.swagger.io/v2/pet/findByStatus?status=sold";

    public static void main(String[] args) {
        List<Tuple> soldPets = fetchSoldPets();
        soldPets.forEach(pet -> System.out.println("{" + pet.id + ", " + pet.name + "}"));
    }

    public static List<Tuple> fetchSoldPets() {
        List<Tuple> soldPets = new ArrayList<>();
        try {
            // Crear cliente HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();

            // Enviar solicitud y recibir respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Procesar JSON con Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            for (JsonNode pet : root) {
                long id = pet.has("id") ? pet.get("id").asLong() : -1;
                String name = pet.has("name") ? pet.get("name").asText() : "Unnamed";
                soldPets.add(new Tuple(id, name));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return soldPets;
    }
}

// Clase para representar la tupla {id, name}
class Tuple {
    long id;
    String name;

    public Tuple(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
