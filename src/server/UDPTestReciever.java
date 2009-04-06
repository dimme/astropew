/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonsturk
 */
public class UDPTestReciever {
    private DatagramSocket socket;
    private boolean running = true;
    private DatagramPacket packet;
    private int counter;

    public UDPTestReciever() {
        packet = new DatagramPacket(new byte[(2<<15) - 1], 2<<15 - 1);
        try {
            socket = new DatagramSocket(30000);

            while(running) {
                packet.setLength(2<<15 - 1);
                socket.receive(packet);
                processPacket(packet);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(UDPTestReciever.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(counter);
    }

    public void processPacket(DatagramPacket p) {
        System.out.println("Packet Recieved from :" + p.getSocketAddress() + " with data: " + new String(p.getData()));
        counter++;
    }

    public static void main( String[] args ) {
        new UDPTestReciever();
    }
}
