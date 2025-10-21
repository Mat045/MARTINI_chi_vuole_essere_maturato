import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ApiClient client = new ApiClient();

        System.out.print("Inserire numero di domande a cui si vuole rispondere: ");
        int amount = scanner.nextInt();

        String json = client.fetchData(amount, "easy", "multiple");
        System.out.println("Risposta JSON dall’API:");
        System.out.println(json);
    }
}
