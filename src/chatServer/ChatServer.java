
package chatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {
    private boolean run = true;
    private List<ChatServerThread> serverThreadsList = new ArrayList();
    private ServerSocket servicio;
    
    public ChatServer(int port) {
        try{
            servicio = new ServerSocket(port); //abre puerto  
        }catch (IOException ex){
            System.out.println("constructor "+ex.getLocalizedMessage());
        }
    }
    
    public void broadcast(String text){
        for(ChatServerThread client: serverThreadsList){
            client.send(text);
        }
    }
    
    public void broadcastTo(String text, String nick){
        for(ChatServerThread client: serverThreadsList){
            if(client.getNick().equalsIgnoreCase(nick)){
                client.send(text+" (Privado)");
            }
        }
    }
    
    public void startService(){
        Thread mainThread = new Thread(){
            public void run(){
                Socket servidor;
                ChatServerThread chatServerThread;
                while(run){
                    try {
                        servidor = servicio.accept();
                        chatServerThread = new ChatServerThread(ChatServer.this ,servidor);
                        serverThreadsList.add(chatServerThread);
                        chatServerThread.setId(serverThreadsList.indexOf(chatServerThread));
                        chatServerThread.start();
                    } catch (IOException ex) {
                        System.out.println("startService "+ex.getLocalizedMessage());
                    }
                }
            }
        };
        
        mainThread.start();
        
        
    }
    
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(5000);
        chatServer.startService();
        
    }

    
    
}
