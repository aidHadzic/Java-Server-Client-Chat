package clientserverchat;

import java.io.*;
import java.net.*;

/**
 *
 * @author aid
 */

public class Server implements Constants {
    //sockets initialization
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;

    //every client is an instance of Client class
    private static final Client[] threads = new Client[MAX_CLIENTS];

    public static void main(String args[]) throws IOException {

        if (args.length < 1) {
            //use the default port 8000
            System.out.println("Usage: java MultiThreadChatServerSync <portNumber>\n" + "Currently using the default port " + DEFAULT_PORT);
            serverSocket = new ServerSocket(DEFAULT_PORT);
        } else {
            //use entered port
            int entered_port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(entered_port);
        }

        //creating clients connections
        while (true) {
            try {
                socket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < MAX_CLIENTS; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new Client(socket, threads)).start();
                        break;
                    }
                }
                if (i == MAX_CLIENTS) {
                    PrintStream os = new PrintStream(socket.getOutputStream());
                    os.println("10 clients already in the chat room. Please try later.");
                    os.close();
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
