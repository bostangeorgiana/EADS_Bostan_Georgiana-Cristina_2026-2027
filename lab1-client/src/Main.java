import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose a value (1 or 2): ");
        String value = scanner.nextLine().trim();

        HttpClient client = HttpClient.newHttpClient();

        // Build the request: GET, and ask for plain text (not HTML)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/lab1/controller?page=" + value))
                .header("Accept", "text/plain")
                .GET()
                .build();

        // Send the request and receive the response
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status code:  " + response.statusCode());
        System.out.println("Content-Type: " + response.headers().firstValue("Content-Type").orElse("-"));
        System.out.println("Response:     " + response.body());
    }
}