/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonsturk
 */
public class UDPTestSender {
    private DatagramPacket packet;
    private DatagramSocket socket;

    private String hostname;

    public UDPTestSender(String hostname) {
        this.hostname = hostname;
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            packet = new DatagramPacket(new byte[(2<<15) - 1], 2<<15 - 1, addr, 30000);
            String testStr = "Testing UDP";
            packet.setData(testStr.getBytes());
            packet.setLength(testStr.length());
            socket = new DatagramSocket();

            socket.send(packet);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPTestSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(UDPTestSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPTestSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        if( args.length == 0 ) {
            new UDPTestSender("localhost");
        } else {
            new UDPTestSender(args[0]);
        }
    }
}
