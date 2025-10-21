import com.google.gson.Gson;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiClient client = new ApiClient();
        Gson gson = new Gson();

        System.out.print("Inserire numero di domande a cui si vuole rispondere: ");
        int amount = scanner.nextInt();
        String json = client.fetchData(amount, "easy", "multiple");
        ApiResponse response = gson.fromJson(json, ApiResponse.class);

        for (ApiQuestion q : response.getResults()) {
            System.out.println("Categoria: " + q.getCategory());
            System.out.println("Domanda: " + q.getQuestion());
            System.out.println("Risposta corretta: " + q.getCorrectAnswer());
            System.out.println("----------------------------------------");
        }
    }
}
