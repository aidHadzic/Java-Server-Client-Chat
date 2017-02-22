package clientserverchat;

import java.io.*;
import java.net.*;

/**
 *
 * @author aid
 */

public class Client extends Thread {

    private String clientName = null;
    
    private DataInputStream inputStream = null;
    private PrintStream outputStream = null;
    
    private Socket clientSocket = null;
    
    private final Client[] threads;
    private int clientsCount;

    public Client(Socket clientSocket, Client[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        clientsCount = threads.length;
    }

    public void run() {
        int clientsCount = this.clientsCount;
        Client[] threads = this.threads;

        try {
            //create input and output streams for this client
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new PrintStream(clientSocket.getOutputStream());
            String name;
            //ask for name with restriction to '@' character because it is used for sending personal messages
            while (true) {
                outputStream.println("Enter your name.");
                name = inputStream.readLine().trim();
                if (name.indexOf('@') == -1) {
                    break;
                } else {
                    outputStream.println("The name should not contain '@' character.");
                }
            }

            //say welcome to client
            outputStream.println("Welcome " + name + " to our chat room.\nTo leave enter /quit in a new line.");
            synchronized (this) {
                for (int i = 0; i < clientsCount; i++) {
                    if (threads[i] != null && threads[i] == this) {
                        clientName = "@" + name;
                        break;
                    }
                }
                for (int i = 0; i < clientsCount; i++) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].outputStream.println("*** A new user " + name + " entered the chat room !!! ***");
                    }
                }
            }
            //start the conversation
            while (true) {
                String line = inputStream.readLine();
                
                //check for '/quit' message
                if (line.startsWith("/quit")) {
                    break;
                }
                //check if message is private
                if (line.startsWith("@")) {
                    //if line starts with @ split the line on words[0] = name and words[1] = message
                    String[] words = line.split("\\s", 2);
                    if (words.length > 1 && words[1] != null) {
                        words[1] = words[1].trim();
                        if (!words[1].isEmpty()) {
                            //make it synchronized because not more than one thread should not access it at the same time
                            synchronized (this) {
                                for (int i = 0; i < clientsCount; i++) {
                                    if (threads[i] != null && threads[i] != this && threads[i].clientName != null && threads[i].clientName.equals(words[0])) {
                                        threads[i].outputStream.println("<" + name + "> " + words[1]);
                                        //print this message to let the client know the private message was sent
                                        this.outputStream.println(">" + name + "< " + words[1]);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //the message is visible to all clients
                else {
                    synchronized (this) {
                        for (int i = 0; i < clientsCount; i++) {
                            if (threads[i] != null && threads[i].clientName != null) {
                                threads[i].outputStream.println("<" + name + "> " + line);
                            }
                        }
                    }
                }
            }
            
            //notify other clients that client is leaving chat room
            synchronized (this) {
                for (int i = 0; i < clientsCount; i++) {
                    if (threads[i] != null && threads[i] != this && threads[i].clientName != null) {
                        threads[i].outputStream.println("*** The user " + name + " is leaving the chat room !!! ***");
                    }
                }
            }
            outputStream.println("*** Bye " + name + " ***");

            //set the value of current thread to null and make free place for other thread to start
            synchronized (this) {
                for (int i = 0; i < clientsCount; i++) {
                    if (threads[i] == this) {
                        threads[i] = null;
                    }
                }
            }
            
            //close the connections
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
