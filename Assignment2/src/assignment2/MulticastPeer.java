package assignment2;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// Class that load GUI and the other services
public class MulticastPeer {

    // Field declaration for GUI, receive service, cast service and answer service
    private PeerGUI peerGUI;
    private ReceiveService receiveService;
    private CastService castService;
    private AnswerService answerService;

    // main funciont
    public static void main(String args[]) {
        // create multicast peer object
        MulticastPeer ms = new MulticastPeer();
        // Create gui
        PeerGUI peerGUI = new PeerGUI(ms);
        // Bind GUI in with mulitcast 
        ms.setPeerGUI(peerGUI);
        // make visible
        peerGUI.setVisible(true);

    }

    // method that takes peerid, multicast group, port no and answer from gui and run the cast service, answer service
    public void runServices(String peerId, String multiCastGroup, int port, int answerPort) {

        try {
            // Field declaraton multicast
            MulticastSocket ms = null;
            InetAddress group = InetAddress.getByName(multiCastGroup);
            // creating multicast socket
            ms = new MulticastSocket(port);
            // joining to the group
            ms.joinGroup(group);
            // reply Ip address
            InetAddress replyIP = InetAddress.getByName("localhost");
            //start the 3 service threads
            castService = new CastService(ms, group, port, peerId, replyIP, answerPort);
            receiveService = new ReceiveService(ms, peerId, group);
            answerService = new AnswerService(answerPort, replyIP);
            
            // bind the GUI class to answer service, cast service
            this.answerService.setPeerGUI(peerGUI);
            this.castService.setPeerGUI(peerGUI);
//            display the query to table
            this.peerGUI.displayQuery(true,false);

        } catch (UnknownHostException ex) {
            Logger.getLogger(MulticastPeer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MulticastPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// bind GUI class
    public void setPeerGUI(PeerGUI peerGUI) {
        this.peerGUI = peerGUI;
    }
// call multicast Question through cast service
    void multiCastQuestions(String question) {
        this.castService.multicastQuestions(question);
    }

   
}
