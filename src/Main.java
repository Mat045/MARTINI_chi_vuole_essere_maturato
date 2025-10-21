import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiClient client = new ApiClient();
        Gson gson = new Gson();

        System.out.print("Inserisci il tuo nome: ");
        String playerName = scanner.nextLine();

        // 🔹 Scarica le domande (5 easy, 3 medium, 2 hard)
        List<ApiQuestion> allQuestions = new ArrayList<>();
        allQuestions.addAll(fetchQuestions(client, gson, 5, "easy"));
        allQuestions.addAll(fetchQuestions(client, gson, 3, "medium"));
        allQuestions.addAll(fetchQuestions(client, gson, 2, "hard"));

        System.out.println("\nBenvenuto, " + playerName + "!");

        int correctAnswers = 0;
        boolean used5050 = false;
        boolean usedAudience = false;

        for (int i = 0; i < allQuestions.size(); i++) {
            ApiQuestion q = allQuestions.get(i);

            System.out.println("Domanda " + (i + 1) + " (" + q.getDifficulty() + ")");
            List<String> answers = new ArrayList<>();
            answers.add(q.getCorrectAnswer());
            answers.addAll(Arrays.asList(q.getIncorrectAnswers()));
            Collections.shuffle(answers);

            System.out.println(q.getQuestion());
            char letter = 'A';
            for (String ans : answers) {
                System.out.println(letter + ") " + ans);
                letter++;
            }

            System.out.println("\nScegli una risposta (A/B/C/D), H per aiuto, R per ritirarti:");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("H")) {
                System.out.println("Scegli aiuto:");
                if (!used5050) System.out.println("1) Guarda il bigliettino (50/50)");
                if (!usedAudience) System.out.println("2) Suggerimento dei compagni");
                System.out.print("Scelta: ");
                String choice = scanner.nextLine();

                if (choice.equals("1") && !used5050) {
                    used5050 = true;
                    use5050(q);
                } else if (choice.equals("2") && !usedAudience) {
                    usedAudience = true;
                    useAudience(q);
                } else
                    System.out.println("Aiuto non disponibile o già usato!");
                i--;
                continue;
            }

            if (input.equals("R")) {
                System.out.println("Domande risposte correttamente: " + correctAnswers);
                break;
            }

            int index = input.charAt(0) - 'A';
            if (index >= 0 && index < answers.size()) {
                if (answers.get(index).equals(q.getCorrectAnswer())) {
                    System.out.println("GIUSTO");
                    correctAnswers++;
                } else {
                    System.out.println("SBAGLIATO");
                    System.out.println("Vera risposta: " + q.getCorrectAnswer());
                    break;
                }
            } else {
                System.out.println("Input non valido");
                i--;
            }
        }

        PlayerStatistics stats = new PlayerStatistics(playerName, correctAnswers, used5050, usedAudience);
        saveStats(stats, gson);

        System.out.println("\nHai risposto correttamente a " + correctAnswers + " domande!");
        System.out.println("Statistiche salvate in player_stats.json");
    }

    private static List<ApiQuestion> fetchQuestions(ApiClient client, Gson gson, int amount, String difficulty) {
        String json = client.fetchData(amount, difficulty, "multiple");
        ApiResponse response = gson.fromJson(json, ApiResponse.class);
        return response.getResults();
    }

    private static void use5050(ApiQuestion q) {
        List<String> wrongs = new ArrayList<>(Arrays.asList(q.getIncorrectAnswers()));
        Collections.shuffle(wrongs);
        System.out.println("\n💡 50/50 attivo! Eliminate due risposte sbagliate:");
        System.out.println("- " + wrongs.get(0));
        System.out.println("- " + wrongs.get(1));
    }

    private static void useAudience(ApiQuestion q) {
        Random r = new Random();
        int correctPerc = 40 + r.nextInt(30); // tra 40% e 70%
        int remaining = 100 - correctPerc;
        System.out.println("\n📊 Suggerimento dei compagni:");
        System.out.println(q.getCorrectAnswer() + ": " + correctPerc + "%");
        System.out.println("Altre risposte: " + remaining + "% totali divise tra le altre.");
    }

    private static void saveStats(PlayerStatistics stats, Gson gson) {
        try (FileWriter writer = new FileWriter("player_stats.json")) {
            gson.toJson(stats, writer);
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio: " + e.getMessage());
        }
    }
}
