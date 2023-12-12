import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

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
            ArrayList<String> answers= new ArrayList<>();
            ArrayList<String> goodAnswers= new ArrayList<>();
            BufferedReader fileReader = new BufferedReader(new FileReader("s.txt"));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inFromCLient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient.println("Podaj imie i nazwisko: ");
            String name = inFromCLient.readLine();
         answers.add(name);

            String line;
            while ((line = fileReader.readLine())!=null) {
                outToClient.println(line);
                for (int i = 0; i < 4; i++) {
                    String option = fileReader.readLine();
                    outToClient.println(option); // Wysyłaj warianty odpowiedzi
                }
                goodAnswers.add(fileReader.readLine().trim());
                String response = inFromCLient.readLine();

                System.out.println("Klient " + clientNumber + " odpowiedzial: " + response);
                answers.add(response);
            }
                try(PrintWriter writer = new PrintWriter(new FileWriter("c.txt",true))){
                    int y=0;
                    for (int i=1;i<answers.size();i++){

                        if(answers.get(i).equals(goodAnswers.get(i-1))){
                            y++;

                        }
                    }
                    outToClient.println("Twój wynik: "+y+"/10");
                       answers.forEach(answer->writer.println(answer));
                }catch (IOException e){
                    e.printStackTrace();
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
