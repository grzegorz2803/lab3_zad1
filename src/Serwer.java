import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serwer {
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Serwer nasłuchuje ....");
            int clientCount = 0;
            while (clientCount <250){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Połączono z klientem "+ (clientCount+1));
                Thread clientThread = new Thread(new ClientHandler(clientSocket, clientCount));
                clientThread.start();
                clientCount++;
            }
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
