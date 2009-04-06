/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.PackageType;
import common.Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonsturk
 */
public class NetworkThread extends Thread{
    DatagramPacket rec;
    DatagramSocket sock;
    DatagramPacket send;

    public NetworkThread(String name, String hostname, int port) {
        try {
            InetAddress iaddr = InetAddress.getByName(hostname);
            byte[] buf = name.getBytes();
            send = new DatagramPacket(buf, buf.length, iaddr, port);
            int size = 65000;
            byte[] buf2 = new byte[size];
            rec = new DatagramPacket(buf2, size);
            sock = null;
            sock = new DatagramSocket();
            sock.send(send);

            int id = -1;
            long randSeed = -1l;
            sock.setSoTimeout(5000);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException e) {
                System.out.println("unable to create socket.");
                System.exit(1);
        } catch (IOException e) {

        }
    }
    public void run() {
        while(true){
            try {
                sock.receive(rec);
                if( rec.getData()[0] == PackageType.INITIALIZER ){
                        int id = Util.bytesToInt(rec.getData(), 1, 4);
                        long randSeed = Util.bytesToLong(rec.getData(),5,8);
                        sock.setSoTimeout(0);
                        System.out.println("Established contact with server. Got id: "+ id +". Got seed: "+ randSeed);
                }
                if( rec.getData()[0] == PackageType.PLAYER_LEFT ){
                        int lid = Util.bytesToInt(rec.getData(), 1, 4);
                        System.out.println("PLayer Left. ID = " + lid);
                }
                if(rec.getData()[0] == PackageType.PLAYER_JOINED ){
                        int nid = Util.bytesToInt(rec.getData(), 1, 4);
                        byte[] bt = new byte[rec.getLength() - 5];
                        System.arraycopy(rec.getData(), 5, bt, 0, bt.length);

                        System.out.println("PLayer Joined. ID = " + nid + " Name = " + new String(bt));
                }
            } catch (SocketTimeoutException e ) {
                    try {
                            sock.send(send);
                    } catch (IOException e1) {
                            System.out.println("IOException: Unable to send packet.");
                            System.exit(1);
                    }
            } catch (IOException e) {
                    System.out.println("IOException: Unable to send packet.");
                    System.exit(1);
            }
        }
    }
}
