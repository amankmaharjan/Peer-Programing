package assignment2;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

// Class that performs casting services
public class CastService extends Thread {

    // Field declaration for cast services
    private PeerGUI peerGUI;
    MulticastSocket ms = null;
    InetAddress group = null;
    int port = 8888;
    String peerId;
    InetAddress replyIP = null;
    int answerPort;

    // Paramaterised constructor
    public CastService(MulticastSocket ms, InetAddress group, int port, String myid, InetAddress replyIp, int answerPort) {

        this.ms = ms;
        this.group = group;
        this.port = port;
        this.peerId = myid;
        this.replyIP = replyIp;
        this.answerPort = answerPort;
        this.start();
    }

    @Override
    public void run() {
        System.out.println("Mulitcast:Thread running");
        System.out.println("Peer id:" + peerId);
        System.out.println("Group port:" + port);

    }
// Method that  multicast questions

    void multicastQuestions(String question) {

        //multicast the message
        this.peerGUI.display("--------------------------------------------------------------------");
        this.peerGUI.display("Question:" + ":" + "" + question);
        String message = new String(peerId + ":" + question + ":" + replyIP.getHostName() + ":" + answerPort);
        byte[] m = message.getBytes();
        DatagramPacket messageOut = new DatagramPacket(m, m.length, group, port);
        try {
            // Send the message
            ms.send(messageOut);
        } catch (IOException ex) {
            Logger.getLogger(CastService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// Bind GUI

    public void setPeerGUI(PeerGUI peerGUI) {
        this.peerGUI = peerGUI;
    }
}
