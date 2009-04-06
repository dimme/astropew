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
public class UDPTestReceiver {
    private DatagramSocket socket;
    private boolean running = true;
    private DatagramPacket packet;
    private int counter;
    private int port;

    public UDPTestReceiver(int port) {
        this.port = port;
        packet = new DatagramPacket(new byte[(2<<15) - 1], 2<<15 - 1);
        try {
            socket = new DatagramSocket(port);

            while(running) {
                socket.receive(packet);
                processPacket(packet);
                packet.setLength(2<<15 - 1);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(UDPTestReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(counter);
    }

    public void processPacket(DatagramPacket p) {
        System.out.println(p.getLength());
        byte[] b = new byte[p.getLength()];
        System.arraycopy(p.getData(), 0, b, 0, b.length);
        System.out.println("Packet Recieved from :" + p.getSocketAddress() + " with data: " + new String(b));
        counter++;
    }

    public static void main( String[] args ) {
        new UDPTestReceiver(30000);
    }
}
