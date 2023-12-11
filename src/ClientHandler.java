import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
private Socket clientSocket;
private  int clientNumber;

public ClientHandler(Socket clientSocket, int clientNumber){
    this.clientSocket = clientSocket;
    this.clientNumber = clientNumber;
}
    @Override
    public void run() {
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader("s.txt"));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inFromCLient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = fileReader.readLine())!=null){
                outToClient.println(line);
                for (int i = 0; i < 4; i++) {
                    String option = fileReader.readLine();
                    outToClient.println(option); // Wysyłaj warianty odpowiedzi
                }
                String response = inFromCLient.readLine();
                System.out.println("Klient "+clientNumber+" odpowiedzial: "+response);

                try(PrintWriter writer = new PrintWriter(new FileWriter("c.txt",true))){
                    writer.println(response);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            fileReader.close();
            inFromCLient.close();
            outToClient.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
