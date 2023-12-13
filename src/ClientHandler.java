import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
private final Socket clientSocket;
private final int clientNumber;
    private int result;


public ClientHandler(Socket clientSocket, int clientNumber){
    this.clientSocket = clientSocket;
    this.clientNumber = clientNumber;
    result=0;
}

    @Override
    public void run() {

        try{
            ArrayList<String> answers= new ArrayList<>();
            ArrayList<String> question = new ArrayList<>();
            ArrayList<String> goodAnswers= new ArrayList<>();
            BufferedReader fileReader = new BufferedReader(new FileReader("bazaPytan.txt"));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inFromCLient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient.println("Podaj imie i nazwisko: ");
            String name = inFromCLient.readLine();


            String line;
            while ((line = fileReader.readLine())!=null) {
                question.add(line);
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
            for (int i=0;i<answers.size();i++){

                if(answers.get(i).equals(goodAnswers.get(i))){
                    result++;

                }
            }
            outToClient.println("Twój wynik: "+result+"/10");
            try(FileWriter fileWriter = new FileWriter("bazaOdpowiedzi.txt",true);
            RandomAccessFile file = new RandomAccessFile("bazaOdpowiedzi.txt","rw");
                FileChannel channel = file.getChannel();
                PrintWriter writer = new PrintWriter(fileWriter)){
                FileLock lock = channel.lock();
                writer.println(name);
                for (int i=0;i<question.size();i++){
                    writer.println(question.get(i)+": "+answers.get(i));
                }
                lock.release();
            }catch (IOException e){
                e.printStackTrace();
            }
            try(PrintWriter writer = new PrintWriter(new FileWriter("wyniki.txt",true));
            RandomAccessFile file = new RandomAccessFile("wyniki.txt","rw");
            FileChannel channel = file.getChannel()){
                FileLock lock = channel.lock();
                writer.println(name +": "+result+"/10");
                lock.release();
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
