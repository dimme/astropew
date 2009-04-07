/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import common.CatastrophicException;
import common.PackageType;
import common.Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonsturk
 */
public class NetworkThread extends Thread{
    private DatagramPacket rec;
    private DatagramSocket sock;
    private DatagramPacket send;

    private boolean connected;
    private boolean running = true;

    private List<NetworkObserver> networkObservers;

    public void addNetworkObserver(NetworkObserver nwo) {
        networkObservers.add(nwo);
    }

    public void notifyNetworkObservers(byte[] data) {
        for( NetworkObserver nwo : networkObservers ) {
            nwo.packetReceived(data);
        }
    }

    public NetworkThread() {
        setConnected(false);
        networkObservers = new LinkedList<NetworkObserver>();
    }

    public NetworkThread(String name, String hostname, int port)  throws CatastrophicException {
        this();
        connect(name, hostname, port);
    }

    public void connect(String name, String hostname, int port) throws CatastrophicException {
        if( isConnected() ) {
            // Todo. Do something.
        }
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

            setConnected(true);
        } catch (Exception ex) {
            Logger.getLogger(NetworkThread.class.getName()).log(Level.SEVERE, null, ex);
            throw new CatastrophicException(ex);
        } 
    }

    public void run() {
        while(isRunning()){
            try {
                sock.receive(rec);
                if( rec.getData()[0] == PackageType.INITIALIZER ){
                    notifyNetworkObservers(rec.getData());
                    sock.setSoTimeout(0);
                    break;
                }
            } catch (SocketTimeoutException e) {
                
            } catch (IOException e ) {
                try {
                    sock.send(send);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
                throw new RuntimeException(e);
            }
        }

        while (isRunning()) {
            try {
                sock.receive(rec);
                byte[] bt = new byte[rec.getLength()];
                System.arraycopy(rec.getData(), 0, bt, 0, bt.length);
                notifyNetworkObservers(bt);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * @param connected the connected to set
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
        if( running == false && sock != null && sock.isConnected()) {
            sock.close();
        }
    }
}
