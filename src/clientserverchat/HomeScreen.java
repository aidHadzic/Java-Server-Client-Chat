package clientserverchat;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class HomeScreen implements Constants {

    public static void main(String[] args) throws IOException {
        
        //variable check whether server is created
        boolean serverCreated = available(DEFAULT_PORT);
        final JPanel panel = new JPanel();
        
        //for gui components
        Object[] options = {"Server", "Client"};
        String initial = "Server";
        String[] arguments;
        
        Object chosen = JOptionPane.showInputDialog(null, "Sign in as: ", "Chat Application", JOptionPane.QUESTION_MESSAGE, null, options, initial);
        
        if (chosen.equals("Server")) {
            //create server
            arguments = new String[]{};
            new Server().main(arguments);
        } else if (chosen.equals("Client")) {
            //if server is not created abort the process
            if (serverCreated) {
                JOptionPane.showMessageDialog(panel, "Server must be created first!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
            
            //create client
            String IPServer = JOptionPane.showInputDialog("Enter the IP address..");
            arguments = new String[]{IPServer};
            new Chat().main(arguments);
        }

    }
    
    //function to check if port is available or not, if it is available than server is not created
    private static boolean available(int port) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
            return false;
        }
        catch (IOException ex) {
                return true;
        }
        
    }

}
