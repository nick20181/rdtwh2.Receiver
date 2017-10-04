package cs255hw2.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class ReceiverClient {

    /**
     * Runs the client as an application. First it displays a dialog box asking
     * for the IP address or hostname of a host running the date server, then
     * connects to it and displays the date that it serves.
     */
    public static void main(String[] args) throws IOException {
        
        Socket s = new Socket("10.100.1.239", 4466);
        BufferedReader input
                = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        System.exit(0);
    }
}
