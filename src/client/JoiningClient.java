package client;

import common.CatastrophicException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import common.MessageType;
import common.PackageType;
import common.Util;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoiningClient {
	

    public static void main(String args[]){
        if(args.length != 3){
                System.out.println("Proper use: port adress name");
                System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        NetworkThread nt;
        try {
            ConsoleNetworkObserver cno = new ConsoleNetworkObserver();
            nt = new NetworkThread(args[2], args[1], port);
            nt.addNetworkObserver(cno);
            nt.start();
        } catch (CatastrophicException ex) {
            Logger.getLogger(JoiningClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (RuntimeException ex) {
            Logger.getLogger(JoiningClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
       //Generate world with the seed. etc..
    }
}
