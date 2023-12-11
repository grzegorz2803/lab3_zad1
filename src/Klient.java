import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Klient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 12345);
            System.out.println("Połączono z serwerem");

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Scanner sc = new Scanner(System.in);

            String line;
            while ((line = inFromServer.readLine()) != null) {
                System.out.println(line); // Wypisz pytanie

                for (int i = 0; i < 4; i++) {
                    String option = inFromServer.readLine(); // Odbierz warianty odpowiedzi
                    System.out.println(option);
                }
                System.out.println();
                Future<String> future = executorService.submit(() -> {
                    String response;
                    synchronized (System.out) { // Dodaj blok synchronizacji
                        response = sc.nextLine();
                        outToServer.println(response);
                        outToServer.flush();
                    }
                    return response; // Zwróć odpowiedź klienta
                });

                try {
                    String response = future.get(20, TimeUnit.SECONDS);

                } catch (TimeoutException e) {
                    System.err.println("Czas upłynął");
                    outToServer.println("e"); // Przesłanie pustej odpowiedzi
                    outToServer.flush();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                future.cancel(true);
            }

            inFromServer.close();
            outToServer.close();
            clientSocket.close();
            executorService.shutdown();
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
