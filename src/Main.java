import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiClient client = new ApiClient();
        Gson gson = new Gson();

        System.out.print("Inserisci il tuo nome: ");
        String playerName = scanner.nextLine();

        System.out.print("Inserire numero di domande a cui si vuole rispondere: ");
        int amount = scanner.nextInt();
        scanner.nextLine(); // consuma il newline

        String json = client.fetchData(amount, "easy", "multiple");
        ApiResponse response = gson.fromJson(json, ApiResponse.class);

        // Simuliamo che il giocatore risponda correttamente a metà delle domande
        int correctAnswers = amount / 2;
        boolean used5050 = true;
        boolean usedAudience = false;

        PlayerStatistics stats = new PlayerStatistics(playerName, correctAnswers, used5050, usedAudience);

        // 🔽 Scriviamo i dati nel file JSON
        try (FileWriter writer = new FileWriter("player_stats.json")) {
            gson.toJson(stats, writer);
            System.out.println("\nStatistiche salvate correttamente nel file player_stats.json!");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }
}
