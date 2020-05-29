/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aman
 */
// Thread class that retrives and display answer in the  GUI
public class AnswerService extends Thread {

    // Field declaratio for answer service
    int answerPort;
    InetAddress host;
    private PeerGUI peerGUI;

    // constructor
    public AnswerService(int answerPort, InetAddress host) {

        this.answerPort = answerPort;
        this.host = host;
        this.start();
    }

    @Override
    public void run() {
        System.out.println("Answer Service:Thread running");
        System.out.println("Answer port:" + answerPort);
        // calls service method
        service();
    }

    public void service() {
        // socket declaraton
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(answerPort, host);
        } catch (SocketException ex) {
            Logger.getLogger(AnswerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                //receive the  messages from the Receiver services
                byte[] buffer = new byte[200];
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(messageIn);
                String answer = new String(messageIn.getData(), 0, messageIn.getLength());
                System.out.println("Answer:" + answer);

                // display answer in GUI
                peerGUI.display(answer);

            } catch (IOException ex) {
                Logger.getLogger(AnswerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // bind GUI
    public void setPeerGUI(PeerGUI peerGUI) {
        this.peerGUI = peerGUI;
    }
}
