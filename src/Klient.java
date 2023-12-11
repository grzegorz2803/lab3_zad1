import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Klient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            Socket clientSocket = new Socket("localhost", 12345);
            System.out.println("Połączono z serwerem");

            BufferedReader inFromSewrver = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);

            String line;
            while ((line = inFromSewrver.readLine())!=null){
                System.out.println(line);
                String response = sc.nextLine();
                outToServer.println(response);
            }
            inFromSewrver.close();
            outToServer.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
