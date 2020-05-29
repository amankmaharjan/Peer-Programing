package assignment2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
// Class that recieves the multicast question and send the answer

public class ReceiveService extends Thread {
// Field declaratoin for the  services  methods

    MulticastSocket ms = null;
    String peerId = null;
    InetAddress group;
    int answerport;
    InetAddress host;

    // Paramaterised constructor
    public ReceiveService(MulticastSocket ms, String peerId, InetAddress group) {
        this.ms = ms;
        this.peerId = peerId;
        this.group = group;
// Start thread
        this.start();
    }

    @Override
    public void run() {
        System.out.println("RecieveService:Thread running");
        System.out.println("Peer id:" + peerId);
        // loads question and answer
        intialiseQuestionsAnswer();
        // load  question and send answer
        processQuestion();
    }
// Method that  receives the multicas questions and sends answer to the peer

    public void processQuestion() {
        try {
            DatagramSocket socket = new DatagramSocket();
            while (true) {
                try {

                    //receive the multicast messages
                    byte[] buffer = new byte[200];
                    DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                    ms.receive(messageIn);
                    String msg = new String(messageIn.getData(), 0, messageIn.getLength());
                    // checking the message from which peerId it's coming
                    String[] split_msg = msg.split(":");
                    if (!peerId.equals(split_msg[0])) {
                        System.out.println(msg);
                        //Reply the message to proper host and ip address
                        String question = split_msg[1];
                        host = InetAddress.getByName(split_msg[2]);
                        answerport = Integer.parseInt(split_msg[3]);

                        // Get the answer 
                        String answer = getAnswer(Peer.valueOf((peerId)), question.trim().toUpperCase());
                        if (answer != null) {
                            answer = peerId + ":" + "" + answer;
                            DatagramPacket messageReply = new DatagramPacket(answer.getBytes(), answer.length(), host, answerport);
                            // send answer
                            socket.send(messageReply);
                        } else {
                            System.out.println("Not found");
                        }
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ReceiveService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(ReceiveService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// Mehtod that compares the peer and question to get the answer

    private String getAnswer(Peer peer, String question) {
        // get answer of given question
        return peerQuestionAnswer.get(peer).get(question);
    }

    // Enum class for different Peers
    public enum Peer {

        PPP1, PPP2, PPP3;

    };
    // Data storage for PeerQueston and answer
    public static EnumMap<Peer, Map<String, String>> peerQuestionAnswer = new EnumMap<>(ReceiveService.Peer.class);

    // method that loads questiona and answer in selected  peer
    private static void intialiseQuestionsAnswer() {

        // intitalising questions and answer  list
        String[] questionsListPeer = new String[]{"What is cloud Computing?", "What is peer-to-peer system?", "What is 5G?", "What is fault tolerance?", "What is smartphone?", "What is distributed system?"};
        String[] answerListPeer1 = new String[]{"",
            null,
            null,
            "Fault-tolerant technlogy is a capabilit of a computer system, electronic system or network to deliver uniinterrupted service, despite one or more of its components failing.",
            "Smartphones are a class of mobile phones and of multi-purpose mobile computing device.",
            "A distributed system is a system whose components are located  on diffrent networkd computers, which communicate and coordinate their actions by passing messsage to one another."
        };
        String[] answerListPeer2 = new String[]{"Cloud computing is the on-demand availability of computer system resources, espcially data storage and computing power, without direct active managment by the user.",
            null,
            "5G is the fifth generaton of cellular network.",
            null,
            "A smarthphone performs many of the functions of a computer, typically having touchscreen interface, Intenet access, and an Operating system capable of running downloaded apps.",
            null};
        String[] answerListPeer3 = new String[]{null,
            "Peer-to-peer is  distributed application architecture that  partitions tasks or workload between peers, peers are equally privileged, equipotent participants in the applicaton.",
            "5G is the fifth generaton of mobile wireless communications which promises to lower latency, offer greater  stability, the ability to connect many more devices at once, and move more data.",
            "Fault toleranc is the property that enables a system to continue operating properly in the event of the failure of some of its components.",
            null,
            null};
// map peers
        Map<String, String> peer1 = new HashMap<>();
        Map<String, String> peer2 = new HashMap<>();
        Map<String, String> peer3 = new HashMap<>();
// loading into peers
        for (int i = 0; i < questionsListPeer.length; i++) {
            peer1.put(questionsListPeer[i].toUpperCase(), answerListPeer1[i]);
            peer2.put(questionsListPeer[i].toUpperCase(), answerListPeer2[i]);
            peer3.put(questionsListPeer[i].toUpperCase(), answerListPeer3[i]);

        }
        // MAP PEER AND THE QUESTIONS & ANSWER
        peerQuestionAnswer.put(ReceiveService.Peer.PPP1, peer1);
        peerQuestionAnswer.put(ReceiveService.Peer.PPP2, peer2);
        peerQuestionAnswer.put(ReceiveService.Peer.PPP3, peer3);

    }

}
